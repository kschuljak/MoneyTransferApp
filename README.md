# Money Transfer App
## Java Console App to Send & Request Money

### Users must register to create an account.   
![image](https://user-images.githubusercontent.com/47723396/210193133-75b98a34-221d-4de3-813d-66a9f1407c70.png)   

### Once registered, users can log-in.  For our purposes, users start with a balance of 1,000 'TE bucks'/ $1,000.00.   
![image](https://user-images.githubusercontent.com/47723396/210193172-8b1c1db4-1352-4c34-b5e5-fd9859754c53.png)   

### A message is displayed if users try to view past transfers without having a transaction history.   
![image](https://user-images.githubusercontent.com/47723396/210193184-bfe65e53-d149-4897-8f63-7d0884aaeaf7.png)   

### If users choose to request money, they are given a list of usernames they can request money from.   
![image](https://user-images.githubusercontent.com/47723396/210193221-5338dbf1-f81d-4e5f-a0e5-d4390b9a419d.png)   

### To request money, users must enter both a username to request money from, and the amount that they are requesting.   
![image](https://user-images.githubusercontent.com/47723396/210193264-b5a1954b-7c74-4330-bf3c-d97b229f3704.png)   

### When requesting money, the other user must apprive the request before the transaction is processed.   
Users may also send money to another user.  Unlike requests, when sending money the tranasaction is automatically approved and processed.   
![image](https://user-images.githubusercontent.com/47723396/210193309-054f1d6f-d075-48bf-843f-e9642a3d0617.png)   

### When viewing past transfers, transactions are sorted into Deposits and Withdrawals, and the display is color-coded.   
![image](https://user-images.githubusercontent.com/47723396/210193410-e294cd04-737f-4ac9-a581-6e3381f61fa0.png)   

### Users may view the details of a specific transfer using the transfer number.   
![image](https://user-images.githubusercontent.com/47723396/210193439-f93df1ef-67ff-4e63-9174-60e870c637f0.png)   

## ---  (logging in as bob)  ---

### Another display of past transfers, color-coded and sorted into Deposits and Withdrawals.   
![image](https://user-images.githubusercontent.com/47723396/210193493-793d09d6-bc6f-495c-86ea-6f4b67b81c89.png)   

### When users choose to view pending requests, the transfer list only displays transfers that are still pending.   
![image](https://user-images.githubusercontent.com/47723396/210193575-179abc6c-23ae-40cf-b152-607ed317615e.png)   

### When choosing to approve or reject pending transfer requests, the list of transfers is further filterd to display only pending Withdrawals.   
![image](https://user-images.githubusercontent.com/47723396/210193629-ef7061cf-8e1a-4633-a240-eed6863427aa.png)   

### Users may enter a transfer number to update that specific pending transfer.   
![image](https://user-images.githubusercontent.com/47723396/210193670-ab95ef0d-b856-4d11-b518-aee57f76ec17.png)   
![image](https://user-images.githubusercontent.com/47723396/210193689-476efc5e-a7c5-4c7c-a9f5-893f9c8b0077.png)   

### When viewing past transfers again, the display is updated to reflect approved/rejected transfers.   
![image](https://user-images.githubusercontent.com/47723396/210193729-c133e597-3352-4de0-9ffa-dac667dcc93c.png)   

### Viewing the current balance also reflects the updated account balance after approving a transfer.   
![image](https://user-images.githubusercontent.com/47723396/210193756-eefe6c5b-12eb-411e-aa70-e69bb4e57732.png)  

## --- Errors ---

### When a user enters an invalid transfer number when trying to view a specific transfer, an error message is displayed.   
![image](https://user-images.githubusercontent.com/47723396/210194055-15a9f513-4f79-4da3-b561-7b4366eed029.png)   

### If a user enters an invalid username to send money to (or request money from), an error message is displayed.   
![image](https://user-images.githubusercontent.com/47723396/210194098-c29f36f7-3841-4b10-91d0-bf1f345131ab.png)   
   
### If a user tries to send an amount greater than their current account balance, an error message is displayed.   
![image](https://user-images.githubusercontent.com/47723396/210194116-3698446a-7bbe-4472-af37-18b9b2a3b1ab.png)   

### If a user tries to request an amount greater than the transfer limit of $99,999.99, an error message is displayed.   
![image](https://user-images.githubusercontent.com/47723396/210194194-4a16b89a-1376-49bb-af33-aecea31fbf1a.png)   

### If a user enters an invalid option from the main menu, an error message is displayed.   
![image](https://user-images.githubusercontent.com/47723396/210194232-9eac31ee-175a-45e6-969b-9671b9fa1a84.png)






