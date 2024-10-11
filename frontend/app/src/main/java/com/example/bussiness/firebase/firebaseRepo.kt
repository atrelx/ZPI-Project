package com.example.bussiness.firebase

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID


object FirebaseRepository {
    private val database = Firebase.database
    private val SALES_REF = database.getReference("sales")
    private val PRODUCTS_REF = database.getReference("products")

    fun getSales(onSalesFetched: (List<SoldProduct>) -> Unit) {
        SALES_REF.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sales = snapshot.children.mapNotNull { saleSnapshot ->
                    val id = saleSnapshot.key ?: return@mapNotNull null
                    val name = saleSnapshot.child("name").getValue(String::class.java) ?: ""
                    val salePrice = saleSnapshot.child("salePrice").getValue(Double::class.java) ?: 0.0
                    val saleDate = saleSnapshot.child("saleDate").getValue(Long::class.java) ?: 0L
                    val imageUrl = saleSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                    val amount = saleSnapshot.child("amount").getValue(Int::class.java) ?: 1
                    val totalPrice = saleSnapshot.child("totalPrice").getValue(Double::class.java) ?: 0.0
                    val characteristics = saleSnapshot.child("characteristics").getValue(object : GenericTypeIndicator<Map<String, Any>>() {})
                    SoldProduct(id, name, salePrice, saleDate, imageUrl, amount, totalPrice, characteristics ?: mapOf())
                }
                onSalesFetched(sales)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "Failed to read value.", error.toException())
            }
        })
    }

    fun getProducts(onProductsReceived: (List<Product>) -> Unit) {
        PRODUCTS_REF.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = snapshot.children.mapNotNull { productSnapshot ->
                    val id = productSnapshot.key ?: return@mapNotNull null
                    val name = productSnapshot.child("name").getValue(String::class.java) ?: ""
                    val price = productSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                    val imageUrl = productSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                    val characteristics = productSnapshot.child("characteristics").getValue(object : GenericTypeIndicator<Map<String, Any>>() {})
                    Product(id, name, price, imageUrl, characteristics ?: mapOf())
                }
                onProductsReceived(products)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun upsertProduct(product: Product) {
        val productID = product.id.ifEmpty { UUID.randomUUID().toString() }
        PRODUCTS_REF.child(productID).setValue(product.toHashMap())
            .addOnSuccessListener {
                Log.d("Firebase", "Product upsert successfully.")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Failed to upsert product.", it)
            }
    }

    fun deleteProduct(productId: String) {
        PRODUCTS_REF.child(productId).removeValue()
            .addOnSuccessListener {
                Log.d("Firebase", "Product successfully deleted.")
            }
            .addOnFailureListener {
                Log.d("Firebase", "Failed to delete product.")
            }
    }

    fun upsertSale(saleProduct: SoldProduct) {
        val saleID = saleProduct.id.ifEmpty { UUID.randomUUID().toString() }
        SALES_REF.child(saleID).setValue(saleProduct.toHashMap())
            .addOnSuccessListener {
                Log.d("Firebase", "Sale upserted successfully.")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Failed to upsert sale.", it)
            }
    }

    fun deleteSale(saleId: String) {
        SALES_REF.child(saleId).removeValue()
            .addOnSuccessListener {
                Log.d("Firebase", "Sale successfully deleted.")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Failed to delete sale.", it)
            }
    }

    fun uploadImageToFirebase(fileUri: Uri?, onUrlReceived: (String) -> Unit) {
        fileUri ?: return

        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("${fileUri.lastPathSegment}")

        val uploadTask = imageRef.putFile(fileUri)
        uploadTask.addOnSuccessListener {
            getDownloadUrl(imageRef, onUrlReceived)
        }.addOnFailureListener {
            onUrlReceived("")
        }
    }

    fun getDownloadUrl(storageRef: StorageReference, onUrlReceived: (String) -> Unit) {
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            onUrlReceived(uri.toString())
        }.addOnFailureListener {
            onUrlReceived("")
        }
    }

}










