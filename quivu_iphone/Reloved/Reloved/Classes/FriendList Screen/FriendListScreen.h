//
//  FriendListScreen.h
//  quivu
//
//  Created by Kamal on 12/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Social/Social.h>
#import <Accounts/Accounts.h>
#import <FacebookSDK/FBGraphUser.h>
#import "FriendListCustomCell.h"

@interface FriendListScreen : UIViewController <UITableViewDataSource, UITableViewDelegate, CellFriendFollowDelegate>
{
    IBOutlet UITableView *tbl_FriendList;
    IBOutlet UILabel *lblFriends;
    
    ACAccount *fbAccount;
    NSMutableArray *arrFacebookFriends;
    
    IBOutlet UIView * view_PopUp;
    IBOutlet UILabel * lblMessage;
}
- (IBAction)BtnBack:(id)sender;

@end
