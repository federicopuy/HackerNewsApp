# HackerNewsApp
Android app that displays Android related news extracted from HackerNews.

### **How to use**

Just download the repo and execute it.

### **Features**
 - **Offline use ->** Fetched stories are stored on a local DB using Room, so if the internet connection is lost, cached stories are displayed.
 - **Swipe to delete on a story** -> if the user deletes a story, the item is removed from the displayed stories, and we also record the id of the deleted story and timestamp on the local DB, so that we can filter this story on future searches.
 - **CleanUpDeletedStories** -> we perform a regular cleanup on deleted stories, so that the database does not grow till infinity.

### **Architecture**

![alt text](https://i.imgur.com/Bz4D4xU.jpg)

### **Libraries Used**
 - Communication between UI Layer and ViewModel -> LiveData
 - Communication between other layers -> Coroutines and Flow
 - DI -> Hilt
 - Remote Data Source -> Retrofit, OkHttp and Gson
 - Local Data Source -> Room 
