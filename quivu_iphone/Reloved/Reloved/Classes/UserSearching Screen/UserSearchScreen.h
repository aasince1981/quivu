//
//  UserSearchScreen.h
//  quivu
//
//  Created by Kamal on 05/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SearchUserCustomCell.h"

@interface UserSearchScreen : UIViewController <UITableViewDataSource,UITableViewDelegate,CellSearchUserDelegate,UITextFieldDelegate>
{
    NSMutableArray * arrSearchUserList;
    IBOutlet UITableView *tbl_SearchUser;
    IBOutlet UITextField *tf_Search;
    IBOutlet UIButton *btnSearchIcon;
    
    IBOutlet UIView * view_PopUp;
    IBOutlet UILabel * lblMessage;
}
- (IBAction)BtnSearchIcon:(id)sender;
- (IBAction)btnBack:(id)sender;

@end
