<p align="center">
  <img src="https://user-images.githubusercontent.com/62979330/185958337-61deaaa0-dec8-4fd0-b689-f7da004bcf3a.png">
</p>

<div align="center">
  <h1>Schedule Manager</h1>
</div>
<div align="center">
Calendar app for schedule management that provides functions to check daily routine and manage schedule through calendar
</div>

## Project Type
+ Toy project

## Development Environment
+ Android Studio Bumblebee 
+ Kotlin 1.6.10

## Application Version
+ minSdkVersion 26
+ targetSdkVersion 32

## Libraries
+ __Jetpack__
  + Room, Navigation, LiveData, ViewModel, DataBinding, DataStore 
+ __Asynchronous programming__
  + Coroutine 
+ __DI framework__
  + Koin
+ __UI__
  + CalendarView
    + [Material CalendarView](https://github.com/prolificinteractive/material-calendarview)
  + Toast
    + [styleabletoast](https://github.com/Muddz/StyleableToast)
  + Bottom Bar
    + [SmoothBottomBar](https://github.com/ibrahimsn98/SmoothBottomBar)
  + Viewbinding management
    + [ViewbindingPropertyDelegate](https://github.com/androidbroadcast/ViewBindingPropertyDelegate)

## Functions
<p align="center">
  <img src="https://user-images.githubusercontent.com/62979330/176653985-b3f84e6c-f27f-4608-84fa-48b38870f2f5.png" width=23% height=23%>
  <img src="https://user-images.githubusercontent.com/62979330/176653799-cc140cc8-6cd5-4823-8e01-17b9d9db4822.png" width=23% height=23%>
  <img src="https://user-images.githubusercontent.com/62979330/176654322-305e2b55-7b3a-48ac-8148-d35c5a4bb8a7.png" width=23% height=23%>
  <img src="https://user-images.githubusercontent.com/62979330/176654433-c44b9485-a585-4af9-a843-a2c176e219b4.png" width=23% height=23%>
</p>
<p align="center">
  <img src="https://user-images.githubusercontent.com/62979330/176653479-eacdecd3-c67b-4c0d-943f-68553bd212a4.png" width=23% height=23%>
  <img src="https://user-images.githubusercontent.com/62979330/176659725-41a8eab6-b2e0-4eb5-a8cb-4bfc46b4b218.png" width=23% height=23%>
  <img src="https://user-images.githubusercontent.com/62979330/176659863-294b668e-f953-445d-9d16-a3b8b60b04e8.png" width=23% height=23%>
  <img src="https://user-images.githubusercontent.com/62979330/176659992-8aa3f01b-8f09-4be6-a3ea-9582c4b9d942.png" width=23% height=23%>
</p>

### 1. TODO
+ Add
  + You can add todo items with the content you want
+ Complete
  + When the task is completed, click the checkbox to mark it as completed
+ Edit or Delete
  + Swipe a todo item from side to side to edit or delete it

### 2. Calendar
+ View Calendar
  + The date with event is marked with a dot 
+ Add event
  + Click the date & ‘+’ button at the top to add a event
  + You can select the importance of event, alarm status
+ Edit or Delete
  + You can edit or delete the event to click it
+ Alarm
  + if you set alarm on the event, notification will occurs at the time you set
  
## Version Test
|API Level|Test|
|------|---|
|32|OK|
|31|OK|
|30|OK|
|29|OK|
|28|OK|
|27|OK|
|26|OK|

## License
+ Image : All icons are resources of [Flaticon.com](https://www.flaticon.com/)
