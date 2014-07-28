//
//  ContactsViewController.h
//  Reloved
//
//  Created by Kamal on 31/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AddressBook/AddressBook.h>
#import <AddressBookUI/AddressBookUI.h>

@interface ContactsViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, ABPeoplePickerNavigationControllerDelegate,ABNewPersonViewControllerDelegate,ABPersonViewControllerDelegate>
{
    IBOutlet UITableView *tbl_contacts;
    NSMutableArray * arrContactList;
     NSMutableArray * arrContactListBackUp;
    ABPersonViewController * personController;
     UINavigationController *newNavigationController;
    
    NSInteger intSelectedTableIndex;
    IBOutlet UILabel *lblInviteFreinds;
}

- (IBAction)btnBack:(id)sender;

@end
