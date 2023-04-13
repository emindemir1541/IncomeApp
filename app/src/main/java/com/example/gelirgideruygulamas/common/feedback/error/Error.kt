package com.example.gelirgideruygulamas.common.feedback.error

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.gelirgideruygulamas.common.feedback.SystemInfo
import com.example.gelirgideruygulamas.common.helper.mes
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.system.exitProcess


class Error(private val context: Context) {

    private var fireStore = FirebaseFirestore.getInstance()

    fun globalErrorCatcher(mContext: Context) {
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            getError(mContext, paramThrowable)
            exitProcess(2)
        }
    }

    fun sendToServer() {
        fireStore.collection("Errors").document(SystemInfo(context).deviceId).collection("DateHelper().currentDateTime.toString()")
            .add(SystemInfo(context).device())
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                context.mes("success")
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
                context.mes(e.toString())
            })


        fireStore.collection("Errors").document(SystemInfo(context).deviceId).collection("DateHelper().currentDateTime.toString()")
            .add(SystemInfo(context).android())
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                context.mes("success")
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
                context.mes(e.toString())
            })

        fireStore.collection("Errors").document(SystemInfo(context).deviceId).collection("DateHelper().currentDateTime.toString()")
            .add(SystemInfo(context).app())
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id)
                context.mes("success")
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
                context.mes(e.toString())
            })

    }

    fun sendSystemInfo() {

    }

    private fun getError(mContext: Context, e: Throwable?) {
        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show()
        Log.e(e.toString(), e?.message.toString())
    }
}


// TODO: 11.10.2022 Float E hatası, gelir gider sınır
// TODO: tarihi geçen gelir gider kartlarındaki kalan günün eksiye düşmesini önle
// TODO: shared preference iki öge kaydetmiyor 
// TODO: saved money çalışmıyor

// TODO: karanlık temadaki sıkıntıları düzelt 
// TODO: zamanı geçen giderlerin checkbox unu kırmızı yap,text renklerini değiştir
// TODO: telefoındaki navigasyon çubuğunun rengini değiştir 
// TODO: gelir ekle layoutundaki arkaplan renklerini değiştir 

// TODO: 11.10.2022 kalan geliri bir sonraki aya aktar
// TODO: 6.10.2022 gelir gider tutarına sınır koy
// TODO: 11.10.2022 uygulama açılırken güzel bir başlangıç yapsın
// TODO: 11.10.2022 döviz bilgilerini internettten çek
// TODO: telefona bildirim gönder 

// TODO: KÜTÜPHANE 
// TODO: update sınıfı 
// TODO: error sınıfı 
// TODO: yoorumlar 
