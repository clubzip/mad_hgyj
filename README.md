# Description

### This package contains three tabs, that is, Gallery tab, Contact tab, and Calendar tab.

## 1. Gallery tab

## 2. Contact tab
The Contact tab prints the list of contact data which is stored in JSON file at its internal storage.
User can add the contact information containing profile image, name, and mobile number by touching '+' button.
The profile image is selected from device's own gallery.
In each contact item activity, user can execute the dial and sms to the data. Additionally, user can remove the data from JSON file.
The Edit button is not implemented yet.

## 3. Calendar tab
Data of the desired period can be received and expressed calender (the program is from 2017 to 2030)
Highlighted on clicked date.
When double-clicking on the date, the memo tab can be opened, and it can be deleted and modified.
Today's date, Saturdays, Sundays highlighted.



# Technical issues

### The tabs are implemented in fragment, and a view pager of MainActivity prints the view page for each fragment. 

## 1. Gallery tab

## 2. Contact tab
This fragment contains custom listview. Thus, it contains 'ListViewAdapter' which extends BaseAdapter and 'ListViewItem'.
ListViewAdapter implements Filterable for searching function. Each ListViewItem has three private member variables that contain the information of name, mobile number, and the name of profile image file. ListViewAdapter creates view for each row of custom listview using this data. Initially, these data are from JSON file at internal storage.

The addition and remove of contact data is implemented by rewrite the JSON file. To prints the changed listview, the activities for addition and remove are start by calling startActivityForResult. And the Fragment renew itself in the method 'onActivityResult.'

The calling and Sending message is implemented with Uri.parse method.

## 3. Calendar tab
Main screen composed of fragment.
Implement the basic form by implementing the API of java.util.calendar
Implement DB by using android Room API
Implement DB's addition and modification process by using Dao
Use Decorate class to decorate dates (inherit DayViewDecorator)
