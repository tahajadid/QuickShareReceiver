# QuickShareReceiver

## Overview
Users when they inserting and moving an image/video/media contents in apps is not always easy. To make it simpler for apps to receive rich content, Android 12 (API level 31) introduced a unified API that lets your app accept content from any source: clipboard, keyboard, and drag and drop.

PS : For backward compatibility with previous Android versions, this API is also available in AndroidX (starting from Core 1.7 and Appcompat 1.4), which we recommend you use when implementing this functionality. [source](https://developer.android.com/guide/topics/input/receive-rich-content) 

## Content
In this sample we need to verify the user's OS first by inserting this block of code in our MainActivity :
```groovy
if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
   // do something..
} else {
  // if the version isn't the API 31 or later
  // there a possibility to the application will not use AndroidX 1.5 or later
  ...
}
```

We created a class "CustomContentReceiver" that extends from <b>OnReceiveContentListener</b>
