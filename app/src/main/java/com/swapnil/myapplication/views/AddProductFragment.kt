package com.swapnil.myapplication.views

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        return view
    }

    private fun initView(view: View) {
        ivSelectImage = view.findViewById(R.id.iv_select_image)
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