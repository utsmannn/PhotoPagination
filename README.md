## Photo App Android Jetpack - Implementation Architecture Component Android Jetpack
Source from Unsplash

### This is app single activity and using:

### Architecture Component
- Navigation Component
- LiveData
- Paging Library
- Room
- Lifecycles
- WorkManager (under development)

### Design Pattern
- Model-View-ViewModel
- Builder Pattern

### Other libraries
- RxJava + RxAndroid - [link](https://github.com/ReactiveX/RxAndroid)
- Fast Android Networking - [link](https://github.com/amitshekhariitbhu/Fast-Android-Networking)
- Glide - [link](https://github.com/bumptech/glide)
- PhotoView - [link](https://github.com/chrisbanes/PhotoView)
- Material Drawer - [link](https://github.com/mikepenz/MaterialDrawer)
- Chrome Custom Tabs - with AndroidX support - [link](https://developer.chrome.com/multidevice/android/customtabs)

---

### Navigation Component
Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app. Android Jetpack's Navigation component helps you implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer. The Navigation component also ensures a consistent and predictable user experience by adhering to an established set of principles. - [Google](https://developer.android.com/guide/navigation/navigation-getting-started)

Basically, navigation components are a new alternative to fragment transactions. So that your application is of good quality with Single Activity. All actions are collected in one xml file and you can simply call the action if needed.

- main_graph.xml - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/res/navigation/main_graph.xml)

### Paging Library
Pagination recyclerview build on DataSource and observing data on viewmodel. Check on code sampe with Rx support

- DataSource - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/data/paged/PhotosDataSource.kt)
- SourceFactory - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/data/factory/PhotosSourceFactory.kt)
- Observing on ViewModel - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/viewmodel/PhotosViewModel.kt#L41)

### Room
The Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite. -[Google](https://developer.android.com/topic/libraries/architecture/room). Check my code for sample with Rx support

- Model - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/db/PhotoRoom.kt)
- Dao - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/db/PhotoDao.kt)
- Repository - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/db/PhotosRepository.kt)
- Database - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/db/PhotoDatabase.kt)
- And implementation using Rx on viewmodel - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/viewmodel/BookmarkViewModel.kt#L39)

### Builder Pattern
Builder pattern is great for classes that can have complex initialization. It typically consists of setting values for multiple variables, some of which may be required with the rest being optional. I Using builder pattern for create get random photo when using some action. Check my code.

- Builder - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/builder/RandomPhotoBuilder.kt)
- You need interface for create listener - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/builder/RandomPhotoListener.kt)
- And implementation - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/services/TileChangerServices.kt#L73)

### Tile Support for Nougat
This app support custom tile in quick setting (check image) and action for random photo (when I creating using builder) and setup wallpaper.

- Sample code tile service - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/services/TileChangerServices.kt)

| Tile Support  |
|---|
| <img src="https://i.ibb.co/7kxGxXp/Screen-Shot-2019-06-06-at-8-54-46-PM.png" alt="screenshot" width="400"> |

### Widget support
For device under nougat, I provide widget when same feature.

- Code for widget - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/widget/ChangerWidget.kt)
- Preference widget in xml - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/res/xml/changer_widget_info.xml)

### Using DownloadManager
This app using download manager default by android and use Broadcast manager for receiving notify complete

- Download file - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/ui/fragment/PhotoFragment.kt#L298)
- You need register broadcast and delegate intent filter with DownloadManager.ACTION_DOWNLOAD_COMPLETE - [code](https://github.com/utsmannn/PhotoPagination/blob/master/app/src/main/java/com/utsman/wallaz/ui/fragment/PhotoFragment.kt#L113)

### Material Design
Material design is beauty for app development, I Using Material Drawer Library by agan Mikepenz for build drawer with amazing ui (chek img and code). This app use chip from material library for place history search and tags.

### Screenshot

| ![](https://i.ibb.co/VMk8wct/Screenshot-20190606-055556.png)  | ![](https://i.ibb.co/KLwzZXF/Screenshot-20190606-055604.png)
|---|---|
| ![](https://i.ibb.co/RDsLSfD/Screenshot-20190606-055810.png)  | ![](https://i.ibb.co/tpYCyv1/Screenshot-20190606-055817.png)
| ![](https://i.ibb.co/kSMHS2q/Screenshot-20190606-055918.png)  | ![](https://i.ibb.co/ZYVgq34/Screenshot-20190606-060349.png)


### LICENSE
```
Copyright 2019 Muhammad Utsman. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```