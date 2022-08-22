package com.example.appointments.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Base64
import com.example.appointments.R
import java.nio.charset.Charset

class Utilities {
    companion object {

        fun shareContent(context: Context, shareContent: String) {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                shareContent)
            sendIntent.type = "text/plain"
            context.startActivity(sendIntent)
        }

        fun encodeBase64(data: String): String {
            return Base64.encodeToString(data.toByteArray(Charset.forName("UTF-8")), Base64.DEFAULT).trim()
        }

        fun showSimpleAlert(context: Context, title: String, messsage: String) {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            builder.setMessage(messsage)

            if (title.isNotEmpty())
                builder.setTitle(title)

            builder.setPositiveButton("Ok"){dialog,_ ->
                dialog.cancel()
            }
            val dialog: AlertDialog = builder.create()

            dialog.show()
        }

        fun removeConfirmationAlert(context: Context, messsage: String, listner: OnRemoveClickListener) {

            val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            builder.setMessage(messsage)
            builder.setPositiveButton("Remove") { _, _ ->
                listner.onOkClick()
            }
            builder.setNegativeButton("Cancel") { _, _ ->
                listner.onCancelClick()
            }

            val dialog: AlertDialog = builder.create()

            dialog.show()
        }

        fun getFormattedPartitionKey(partitionKey: String): String {
            return "[\"$partitionKey\"]"
        }


    }

    interface OnRemoveClickListener {
        fun onOkClick()
        fun onCancelClick()
    }


}