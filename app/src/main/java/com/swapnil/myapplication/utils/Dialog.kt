package com.swapnil.myapplication.utils

import android.app.Dialog
import android.content.Context
import com.swapnil.myapplication.R

fun showLoadingDialog(context: Context): Dialog {
    val loadingDialog = Dialog(context, R.style.TransparentDialog)
    loadingDialog.setContentView(R.layout.progress_dialog)
    loadingDialog.setCancelable(false)
    loadingDialog.show()
    return loadingDialog
}

fun hideLoadingDialog(loadingDialog: Dialog?) {
    loadingDialog?.dismiss()
}