# Swipe
## About 
It is a simple app which show random data from the server, The data has image, Price, Type, Name, Tax.
- It offline capable 😃
- Clean and Simple Material UI.

## UPDATE 
Added Dependency Inject, which Can be found in the branch [master-DI](https://github.com/DonutsDevil/Swipe/tree/master-DI)

## Build with 🛠
- Kotlin
- Android Architecture Components - Collection of libraries that help you design robust, testable, and maintainable apps.
    - LiveData - Data objects that notify views when the underlying database changes.
    - ViewModel - Stores UI-related data that isn't destroyed on UI changes.
    - Room - SQLite object mapping library.
    - Navigation Component - Navigating Between Fragment.
    - DiffUtil - Efficient updatation of RecyclerView items by calculating and animating the differences between two lists. 
- Repository Pattern
- MVVM
- Single Activity
- Coroutines

## Package Structure
```
com.swapnil.myapplication                  # Root Package
.
├── constants                              # constant variables
|
├── model                                  # Model classes
|
├── network                                # For data handling.             
│   ├── product                            # Product Packages
│   ├    ├── productApi                    # RetroFit Service
│   ├    ├── ProductNetworkService         # Server side Service
|   |        
│   └── RetryManager                       # Retries API call if failure
|   └── SwipeNetwork                       # Creates Retrofit Instances
|
├── repository                             # For data handling
|   │── ProductRepository                  # Handles network call and local db calls
|   └── State                              # States used by UI to display relevant information
|
|── utils                                  # Shared viewHolder betwwen home,main,details,history
|   │── Dialog                             # Interact with dialogs
|   │── NetworkUtils                       # Internet Notifier and internet state change util
|   └── ProductAdapter                     # A recycler view adapter with DiffUtil implementation
|
├── viewmodel                             # ViewModel Classes
|   └──  ProductViewModel                 # Shared viewHolder betwwen Addition and Getting All Product Fragment
|                    
|── views
    │── AddProductFragment                 # UI for Addition of Product Data
    │── MainActivity                       # Host of other all Fragments
    └── ProductListingFragment             # UI for showing all products
```

## Architecture
This app uses [MVVM (Model View View-Model)](https://developer.android.com/jetpack/guide#recommended-app-arch) architecture.
<img src="https://camo.githubusercontent.com/be9b0e8eb62e13a031031fb16e99f33c647da47908a21f451cb00d2c99fb9b31/68747470733a2f2f646576656c6f7065722e616e64726f69642e636f6d2f746f7069632f6c69627261726965732f6172636869746563747572652f696d616765732f66696e616c2d6172636869746563747572652e706e67"/>
