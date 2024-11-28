package com.zpi.amoz.services;

import com.zpi.amoz.dtos.*;
import com.zpi.amoz.models.*;
import com.zpi.amoz.repository.CompanyRepository;
import com.zpi.amoz.repository.InvoiceRepository;
import com.zpi.amoz.repository.ProductOrderRepository;
import com.zpi.amoz.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {


    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> findById(UUID id) {
        return invoiceRepository.findById(id);
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public boolean deleteById(UUID id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public void sendInvoiceToCustomer(Invoice invoice, String senderUserId) {
        byte[] invoicePDFBytes;

        User user = userRepository.findById(senderUserId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find user for given sub"));

        CustomerDTO customer;
        if (invoice.getProductOrder().getCustomer().getCustomerB2B() != null) {
            InvoiceB2BDTO invoiceB2B = InvoiceB2BDTO.toInvoiceB2BDTO(invoice);
            invoicePDFBytes = generatePDFFromInvoiceB2B(invoiceB2B);
            customer = invoiceB2B.customerB2B().customer();
        } else {
            InvoiceB2CDTO invoiceB2C = InvoiceB2CDTO.toInvoiceB2CDTO(invoice);
            invoicePDFBytes = generatePDFFromInvoiceB2C(invoiceB2C);
            customer = invoiceB2C.customerB2C().customer();
        }
        String email = customer.contactPerson().emailAddress()
                .orElseThrow(() -> new EntityNotFoundException("Customer email address is not given"));

        Company company = companyRepository.getCompanyByCustomerId(customer.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer is not associated with any company"));

        String subject = "Invoice no. " + invoice.getInvoiceNumber() + " from " + company.getName();
        String htmlContent =
                "<html>" +
                        "<body>" +
                        "<p>Dear Customer,</p>" +
                        "<p>Please find attached invoice number <strong>" + invoice.getInvoiceNumber() + "</strong>, issued on <strong>" + invoice.getIssueDate() + "</strong>, for the services provided by our company <strong>" + company.getName() + "</strong>.</p>" +
                        "<p>Kindly arrange payment by <strong>" + invoice.getIssueDate().plusDays(14) + "</strong>.</p>" +
                        "<p>If you have any questions or require any assistance regarding this invoice, please feel free to contact us.</p>" +
                        "<p>Best regards,</p>" +
                        "<p>" + user.getEmployee().getPerson().getName() + " " + user.getEmployee().getPerson().getSurname() + "<br>" +
                         company.getName() + "<br>" +
                        "Phone number: " + user.getEmployee().getContactPerson().getContactNumber();
        if (user.getEmployee().getContactPerson().getEmailAddress() != null) {
            htmlContent += "<br>Email: " + user.getEmployee().getContactPerson().getEmailAddress() + "</p>";
        } else {
            htmlContent += "</p>";
        }
        htmlContent +=
                "</body>" +
                        "</html>";

        emailService.sendEmailWithAttachment(
                Collections.singletonList(email),
                subject,
                htmlContent,
                invoicePDFBytes,
                "invoice_" + invoice.getInvoiceNumber() + ".pdf"
        );
    }


    public byte[] generatePDFFromInvoiceB2C(InvoiceB2CDTO invoiceB2C) {
        String html = generateInvoiceB2CHTML(invoiceB2C);
        return pdfService.generatePDFFromHTML(html);
    }


    public byte[] generatePDFFromInvoiceB2B(InvoiceB2BDTO invoiceB2B) {
        String html = generateInvoiceB2BHTML(invoiceB2B);
        return pdfService.generatePDFFromHTML(html);
    }

    @Transactional
    public Invoice generateInvoice(UUID productOrderId) {
        ProductOrder productOrder = productOrderRepository.findById(productOrderId).orElseThrow(() -> new EntityNotFoundException("Could not find product order for given id: " + productOrderId));

        if (productOrder.getCustomer() == null) {
            throw new EntityNotFoundException("Customer is required to generate an invoice");
        }

        if (productOrder.getInvoice() != null) {
            invoiceRepository.delete(productOrder.getInvoice());
        }

        Invoice invoice = this.createInvoice(productOrder);
        productOrder.setInvoice(invoice);
        invoiceRepository.flush();
        entityManager.refresh(invoice);
        return invoice;
    }

    private Invoice createInvoice(ProductOrder productOrder) {
        Invoice invoice = new Invoice();
        invoice.setAmountOnInvoice(productOrder.getTotalDue());
        return invoiceRepository.save(invoice);
    }

    public String generateInvoiceB2BHTML(InvoiceB2BDTO invoiceB2B) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Invoice</title><style>")
                .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; position: relative; }")
                .append("h2 { text-align: center; }")
                .append("img { width: 150px; height: auto; position: absolute; top: 20px; left: 20px; }")
                .append(".invoice-details { position: absolute; top: 20px; right: 20px; text-align: right; }")
                .append(".container { margin-top: 80px; }")
                .append(".left, .right { display: inline-block; vertical-align: top; width: 45%; }")
                .append(".left { padding-right: 20px; }")
                .append(".right { padding-left: 20px; }")
                .append(".section { margin-top: 30px; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append("table, th, td { border: 1px solid #000; padding: 8px; text-align: left; }")
                .append("th { background-color: #f2f2f2; }")
                .append("</style></head><body>")

                .append("<div style='position: relative; height: 100px;'>")
                .append("<img src='static/amoz.png' alt='Logo' />")

                .append("<div class='invoice-details'>")
                .append("<p><strong>Invoice No:</strong> ").append(invoiceB2B.invoiceNumber()).append("</p>")
                .append("<p><strong>Issue Date:</strong> ").append(invoiceB2B.issueDate()).append("</p>")
                .append("</div>")
                .append("</div>")

                .append("<div class='container'>")

                .append("<div class='left'>")
                .append("<h3>Seller Details</h3>")
                .append("<p><strong>Company Name:</strong> ").append(invoiceB2B.company().name()).append("</p>")
                .append("<p><strong>Company Number:</strong> ").append(invoiceB2B.company().companyNumber()).append("</p>");
        if (invoiceB2B.company().regon().isPresent()) {
            html.append("<p><strong>REGON:</strong> ")
                    .append(invoiceB2B.company().regon().get()).append("</p>");
        }
        html.append("<p><strong>Address</strong>:")
                .append("<div>").append(invoiceB2B.company().address().street()).append(", ")
                .append(invoiceB2B.company().address().streetNumber());

        if (invoiceB2B.company().address().apartmentNumber().isPresent()) {
            html.append("/").append(invoiceB2B.company().address().apartmentNumber().get());
        }
        html.append("</div><div>");
        html.append(invoiceB2B.company().address().city()).append(", ")
                .append(invoiceB2B.company().address().postalCode()).append("</div><div>")
                .append(invoiceB2B.company().address().country()).append("</div></p>")
                .append("</div>")

                .append("<div class='right'>")
                .append("<h3>Customer Details</h3>")
                .append("<p><strong>Name:</strong> ").append(invoiceB2B.customerB2B().nameOnInvoice()).append("</p>")
                .append("<p><strong>Company Number:</strong> ").append(invoiceB2B.customerB2B().companyNumber()).append("</p>")
                .append("<p><strong>Contact:</strong>")
                .append("<div><strong>Phone Number</strong>: ").append(invoiceB2B.customerB2B().customer().contactPerson().contactNumber()).append("</div>");
        if (invoiceB2B.customerB2B().customer().contactPerson().emailAddress().isPresent()) {
            html.append("<div><strong>E-mail:</strong> ").append(invoiceB2B.customerB2B().customer().contactPerson().emailAddress().get()).append("</div></p>");
        } else {
            html.append("</p>");
        }

        html.append("<p><strong>Address</strong>:")
                .append("<div>").append(invoiceB2B.customerB2B().address().street()).append(", ")
                .append(invoiceB2B.customerB2B().address().streetNumber());

        if (invoiceB2B.customerB2B().address().apartmentNumber().isPresent()) {
            html.append("/").append(invoiceB2B.customerB2B().address().apartmentNumber().get());
        }
        html.append("</div><div>");
        html.append(invoiceB2B.customerB2B().address().city()).append(", ")
                .append(invoiceB2B.customerB2B().address().postalCode()).append("</div><div>")
                .append(invoiceB2B.customerB2B().address().country()).append("</div></p>")

                .append("</div>")

                .append("</div>")

                .append("<div class='section'>")
                .append("<h3>Items</h3>")
                .append("<table>")
                .append("<tr><th>Product Name</th><th>Amount</th><th>Cost</th></tr>");

        for (ProductOrderItemSummaryDTO item : invoiceB2B.orderItems()) {
            html.append("<tr>")
                    .append("<td>").append(item.productName()).append("</td>")
                    .append("<td>").append(item.amount()).append("</td>")
                    .append("<td>").append(item.unitPrice().multiply(BigDecimal.valueOf(item.amount())).toPlainString()).append(" PLN</td>")
                    .append("</tr>");
        }

        html.append("</table>")
                .append("</div>")
                .append("<div class='section'>")
                .append("<h3>Total Cost</h3>")
                .append("<p><strong>Total Cost:</strong> ").append(invoiceB2B.amountOnInvoice()).append(" PLN</p>")
                .append("</div>")

                .append("</body></html>");

        return html.toString();
    }


    public String generateInvoiceB2CHTML(InvoiceB2CDTO invoiceB2C) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Invoice</title><style>")
                .append("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; position: relative; }")
                .append("h2 { text-align: center; }")
                .append("img { width: 150px; height: auto; position: absolute; top: 20px; left: 20px; }")
                .append(".invoice-details { position: absolute; top: 20px; right: 20px; text-align: right; }")
                .append(".container { margin-top: 80px; }")
                .append(".left, .right { display: inline-block; vertical-align: top; width: 45%; }")
                .append(".left { padding-right: 20px; }")
                .append(".right { padding-left: 20px; }")
                .append(".section { margin-top: 30px; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }")
                .append("table, th, td { border: 1px solid #000; padding: 8px; text-align: left; }")
                .append("th { background-color: #f2f2f2; }")
                .append("</style></head><body>")

                .append("<div style='position: relative; height: 100px;'>")
                .append("<img src='static/amoz.png' alt='Logo' />")

                .append("<div class='invoice-details'>")
                .append("<p><strong>Invoice No:</strong> ").append(invoiceB2C.invoiceNumber()).append("</p>")
                .append("<p><strong>Issue Date:</strong> ").append(invoiceB2C.issueDate()).append("</p>")
                .append("</div>")
                .append("</div>")

                .append("<div class='container'>")

                .append("<div class='left'>")
                .append("<h3>Seller Details</h3>")
                .append("<p><strong>Company Name:</strong> ").append(invoiceB2C.company().name()).append("</p>")
                .append("<p><strong>Company Number:</strong> ").append(invoiceB2C.company().companyNumber()).append("</p>");
        if (invoiceB2C.company().regon().isPresent()) {
            html.append("<p><strong>REGON:</strong> ")
                    .append(invoiceB2C.company().regon().get()).append("</p>");
        }
        html.append("<p><strong>Address</strong>:")
                .append("<div>").append(invoiceB2C.company().address().street()).append(", ")
                .append(invoiceB2C.company().address().streetNumber());

        if (invoiceB2C.company().address().apartmentNumber().isPresent()) {
            html.append("/").append(invoiceB2C.company().address().apartmentNumber().get());
        }
        html.append("</div><div>");
        html.append(invoiceB2C.company().address().city()).append(", ")
                .append(invoiceB2C.company().address().postalCode()).append("</div><div>")
                .append(invoiceB2C.company().address().country()).append("</div></p>")
                .append("</div>")

                .append("<div class='right'>")
                .append("<h3>Customer Details</h3>")
                .append("<p><strong>Name:</strong> ").append(invoiceB2C.customerB2C().person().surname()).append(" ")
                .append(invoiceB2C.customerB2C().person().name()).append("</p>")
                .append("<p><strong>Date of Birth:</strong> ").append(invoiceB2C.customerB2C().person().dateOfBirth()).append("</p>")
                .append("<p><strong>Contact:</strong>")
                .append("<div><strong>Phone Number</strong>: ").append(invoiceB2C.customerB2C().customer().contactPerson().contactNumber()).append("</div>");
        if (invoiceB2C.customerB2C().customer().contactPerson().emailAddress().isPresent()) {
            html.append("<div><strong>E-mail:</strong> ").append(invoiceB2C.customerB2C().customer().contactPerson().emailAddress().get()).append("</div></p>");
        } else {
            html.append("</p>");
        }

        html.append("</div>")

                .append("</div>")

                .append("<div class='section'>")
                .append("<h3>Items</h3>")
                .append("<table>")
                .append("<tr><th>Product Name</th><th>Amount</th><th>Cost</th></tr>");

        for (ProductOrderItemSummaryDTO item : invoiceB2C.orderItems()) {
            html.append("<tr>")
                    .append("<td>").append(item.productName()).append("</td>")
                    .append("<td>").append(item.amount()).append("</td>")
                    .append("<td>").append(item.unitPrice().multiply(BigDecimal.valueOf(item.amount())).toPlainString()).append(" PLN</td>")
                    .append("</tr>");
        }

        html.append("</table>")
                .append("</div>")
                .append("<div class='section'>")
                .append("<h3>Total Cost</h3>")
                .append("<p><strong>Total Cost:</strong> ").append(invoiceB2C.amountOnInvoice()).append(" PLN</p>")
                .append("</div>")

                .append("</body></html>");

        return html.toString();
    }
}

