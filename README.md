# iTunes Store App

## MVVVM Architecture
#### MVVM Architecture Pros:
1. Optimized data manipulation using LiveData and Multi-threading.
2. Repository for single source of truth for all app data
3. Consistent design every module
4. Clean API and Database calls

## Persistence
#### Room Persistence
Used for saving wish list, as room persistence is 
great for saving complex and huge amount of data.

#### Shared Preferences
Used for saving Last Visit, as it is easy to use and 
is fit for saving and replacing single value datas.


*for LastVisit feature, use Android back button to close app*

## ResourceRequestScreen

Since single activity is used (*MainActivity*), ResourceRequestScreen
helps customize and invoke components from MainActivity such as Toolbar,
AlertDialogs, changing toolbar title, toggle toolbar visibility and, toggle 
toolbar back button on every fragment.

