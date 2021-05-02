package com.example.camerax.utils

import android.content.Context
import android.content.DialogInterface
import android.os.Message
import android.view.View
import android.widget.Button
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.camerax.R
import com.example.camerax.databinding.FragmentAlertBinding

typealias OnClickListener = (() -> Unit)

class AlertDialog(context: Context) : AlertDialog(context, R.style.Dialog_Alert) {
    private val viewDataBinding: FragmentAlertBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.fragment_alert, null, false)

    init {
        setView(viewDataBinding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun setMessage(message: CharSequence?) {
        viewDataBinding.tvTitle.text = message
    }

    override fun setButton(
        whichButton: Int,
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ) {
        val button = getButton(whichButton) ?: return
        button.text = text
        button.visibility = if (text != null) View.VISIBLE else View.GONE
        button.setOnClickListener { _ ->
            listener?.onClick(this, whichButton)
        }
    }

    override fun setButton(whichButton: Int, text: CharSequence?, msg: Message?) {
        val button = getButton(whichButton) ?: return
        button.text = text
        button.visibility = if (text != null) View.VISIBLE else View.GONE
    }

    override fun getButton(whichButton: Int): Button? {
        return when (whichButton) {
            DialogInterface.BUTTON_POSITIVE -> viewDataBinding.btnPositive
            DialogInterface.BUTTON_NEGATIVE -> viewDataBinding.btnNegative
            else -> null
        }
    }

    class Builder(
        private val context: Context
    ) {
        private var message: String = ""
        private var positiveText: String = ""
        private var negativeText: String? = null
        private var onPositiveClickListener: OnClickListener? = null
        private var onNegativeClickListener: OnClickListener? = null

        fun setMessage(message: String): Builder {
            return this.apply {
                this.message = message
            }
        }

        fun setMessage(@StringRes resId: Int): Builder {
            return this.apply {
                setMessage(context.getString(resId))
            }
        }

        fun setPositiveButton(text: String, onClickListener: OnClickListener? = null): Builder {
            return this.apply {
                positiveText = text
                onPositiveClickListener = onClickListener
            }
        }

        fun setPositiveButton(
            @StringRes resId: Int,
            onClickListener: OnClickListener? = null
        ): Builder {
            return this.apply {
                setPositiveButton(context.getString(resId), onClickListener)
            }
        }

        fun setNegativeButton(text: String, onClickListener: OnClickListener? = null): Builder {
            return this.apply {
                negativeText = text
                onNegativeClickListener = onClickListener
            }
        }

        fun setNegativeButton(
            @StringRes resId: Int,
            onClickListener: OnClickListener? = null
        ): Builder {
            return this.apply {
                setNegativeButton(context.getString(resId), onClickListener)
            }
        }

        fun create(): AlertDialog {
            return com.example.camerax.utils.AlertDialog(context).apply {
                setMessage(message)
                val listener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            onPositiveClickListener?.invoke()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            onNegativeClickListener?.invoke()
                        }
                    }

                    dialog.dismiss()
                }
                setButton(DialogInterface.BUTTON_POSITIVE, positiveText, listener)
                setButton(DialogInterface.BUTTON_NEGATIVE, negativeText, listener)
            }
        }

        fun show(): AlertDialog {
            return create().also { dialog ->
                dialog.show()
            }
        }
    }
}