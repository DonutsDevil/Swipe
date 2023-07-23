package com.swapnil.myapplication.views

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.swapnil.myapplication.R
import com.swapnil.myapplication.network.product.ProductNetworkServiceImpl
import com.swapnil.myapplication.repository.ProductRepository
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_product, container, false)
        productViewModel = ViewModelProvider(
            requireActivity(),
            ProductViewModelFactory(ProductRepository((ProductNetworkServiceImpl())))
        )[ProductViewModel::class.java]
        initView(view)
        ivSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }
        setProductLabelAndEditText()
        return view
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

        etProductPrice.inputType = InputType.TYPE_CLASS_NUMBER
        etProductTax.inputType = InputType.TYPE_CLASS_NUMBER

        etProductPrice.setHint(R.string.product_price_hint)
        etProductTax.setHint(R.string.product_tax_hint)
        etProductName.setHint(R.string.product_name_hint)
        etProductType.setHint(R.string.product_type_hint)

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
        ivSelectImage.setImageURI(croppedUri)
    }
}