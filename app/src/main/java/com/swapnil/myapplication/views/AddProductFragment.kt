package com.swapnil.myapplication.views

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.swapnil.myapplication.R
import com.swapnil.myapplication.local.product.ProductLocalServiceImpl
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.network.product.ProductNetworkServiceImpl
import com.swapnil.myapplication.repository.AddProductErrors
import com.swapnil.myapplication.repository.ProductRepository
import com.swapnil.myapplication.repository.State
import com.swapnil.myapplication.utils.hideLoadingDialog
import com.swapnil.myapplication.utils.showLoadingDialog
import com.swapnil.myapplication.viewmodel.ProductViewModel
import com.swapnil.myapplication.viewmodel.ProductViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

private const val TAG = "AddProductFragment"

class AddProductFragment : Fragment() {
    private val CROPPED_IMAGE_FILE_NAME = "cropped_image"

    private lateinit var productViewModel: ProductViewModel

    private lateinit var ivSelectImage: ImageView
    private lateinit var btnAddProduct: Button

    private lateinit var tvProductName: TextView
    private lateinit var etProductName: EditText

    private lateinit var tvProductType: TextView
    private lateinit var etProductType: EditText

    private lateinit var tvProductPrice: TextView
    private lateinit var etProductPrice: EditText

    private lateinit var tvProductTax: TextView
    private lateinit var etProductTax: EditText

    private var progressBarDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_product, container, false)
        productViewModel = ViewModelProvider(
            requireActivity(),
            ProductViewModelFactory(ProductRepository(ProductNetworkServiceImpl(), ProductLocalServiceImpl()))
        )[ProductViewModel::class.java]
        initView(view)
        ivSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }
        setProductLabelAndEditText()
        btnAddProduct.setOnClickListener {
            productViewModel.addProduct(
                name = etProductName.text?.toString(),
                type = etProductType.text?.toString(),
                price = etProductPrice.text?.toString(),
                tax = etProductTax.text?.toString()
            )
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        productViewModel.addProduct.observe(this) { state ->
            when(state) {
                is State.Loading -> {
                    showProgressState(View.VISIBLE)
                }

                is State.Error -> {
                    showProgressState(View.INVISIBLE)
                    setError(state)
                }

                is State.Success -> {
                    productViewModel.clearAddProductCache()
                    Toast.makeText(requireContext(), "Successfully added Product", Toast.LENGTH_SHORT).show()
                    clearFields()
                }

                is State.Reset -> {
                    // do nothing
                    showProgressState(View.INVISIBLE)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        productViewModel.setProductValues(
            name = etProductName.text?.toString(),
            type = etProductType.text?.toString(),
            price = etProductPrice.text?.toString(),
            tax = etProductTax.text?.toString()
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        productViewModel.clearAddProductCache()
    }

    private fun initView(view: View) {
        ivSelectImage = view.findViewById(R.id.iv_select_image)
        btnAddProduct = view.findViewById(R.id.btn_add_product)

        val llProductName: LinearLayout = view.findViewById(R.id.productName)
        tvProductName = llProductName.findViewById(R.id.tv_label)
        etProductName = llProductName.findViewById(R.id.et_label_ans)

        val llProductType: LinearLayout = view.findViewById(R.id.productType)
        tvProductType = llProductType.findViewById(R.id.tv_label)
        etProductType = llProductType.findViewById(R.id.et_label_ans)

        val llProductPrice: LinearLayout = view.findViewById(R.id.productPrice)
        tvProductPrice = llProductPrice.findViewById(R.id.tv_label)
        etProductPrice = llProductPrice.findViewById(R.id.et_label_ans)

        val llProductTax: LinearLayout = view.findViewById(R.id.productTax)
        tvProductTax = llProductTax.findViewById(R.id.tv_label)
        etProductTax = llProductTax.findViewById(R.id.et_label_ans)
    }

    private fun setProductLabelAndEditText() {
        tvProductName.text = getString(R.string.product_name_label)
        tvProductType.text = getString(R.string.product_type_label)
        tvProductPrice.text = getString(R.string.product_price_label)
        tvProductTax.text = getString(R.string.product_tax_label)

        etProductPrice.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        etProductTax.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        etProductPrice.setHint(R.string.product_price_hint)
        etProductTax.setHint(R.string.product_tax_hint)
        etProductName.setHint(R.string.product_name_hint)
        etProductType.setHint(R.string.product_type_hint)

        if (!TextUtils.isEmpty(productViewModel.productName)) {
            etProductName.setText(productViewModel.productName)
        }

        if (!TextUtils.isEmpty(productViewModel.productType)) {
            etProductType.setText(productViewModel.productType)
        }

        if (!TextUtils.isEmpty(productViewModel.productPrice)) {
            etProductPrice.setText(productViewModel.productPrice)
        }

        if (!TextUtils.isEmpty(productViewModel.productTax)) {
            etProductTax.setText(productViewModel.productTax)
        }

        if (productViewModel.productImageUri != null) {
            ivSelectImage.setImageURI(productViewModel.productImageUri)
        }
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data
                val selectedImageUri = data!!.data
                Log.d(TAG, "imagePickerLauncher: selectedImageUri: $selectedImageUri")
                if (selectedImageUri != null) {
                    // Start the cropping activity
                    startCropActivity(selectedImageUri)
                }
            }
        }

    private val cropperLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                // Image cropped successfully
                handleCroppedImage(result.data!!)
            }
        }

    private fun startCropActivity(sourceUri: Uri) {
        val croppedImageFile = File(
            requireActivity().applicationContext.cacheDir,
            CROPPED_IMAGE_FILE_NAME + UUID.randomUUID() + ".png"
        )

        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setToolbarTitle("Crop Image")
        options.withAspectRatio(1F, 1F)

        val cropIntent = UCrop.of(sourceUri, Uri.fromFile(croppedImageFile))
            .withOptions(options)
            .getIntent(requireContext())

        cropperLauncher.launch(cropIntent)
    }

    private fun handleCroppedImage(data: Intent) {
        // The cropped image is available in the 'data' Intent
        val croppedUri = UCrop.getOutput(data)
        Log.d(TAG, "handleCroppedImage: croppedUri: $croppedUri")
        productViewModel.setProductValues(imageUri = croppedUri)
        ivSelectImage.setImageURI(croppedUri)
    }

    private fun setError(state: State.Error<ProductResponse>) {
        when(state.errorMessage) {
            AddProductErrors.NAME.name -> {
                etProductName.error = AddProductErrors.NAME.errorMessage
            }

            AddProductErrors.TYPE.name -> {
                etProductType.error = AddProductErrors.TYPE.errorMessage
            }

            AddProductErrors.PRICE.name -> {
                etProductPrice.error = AddProductErrors.PRICE.errorMessage
            }

            AddProductErrors.TAX.name -> {
                etProductTax.error = AddProductErrors.TAX.errorMessage
            }
            else -> {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        etProductTax.text = null
        etProductPrice.text = null
        etProductName.text = null
        etProductType.text = null
        ivSelectImage.setImageResource(R.drawable.select_image)
    }

    private fun showProgressState(state: Int) {
        if (state == View.VISIBLE) {
            progressBarDialog = showLoadingDialog(requireContext())
        } else {
            hideLoadingDialog(progressBarDialog)
        }
    }

}