//
//  FollowingViewController.h
//  Reloved
//
//  Created by Kamal on 16/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FollowingTabCustomCell.h"

@interface FollowingViewController : UIViewController <CellFunctionTabDelegate,UITableViewDataSource,UITableViewDelegate>
{
    IBOutlet UITableView *tbl_Follow;
    NSMutableArray * arrFollowingUser;
    BOOL boolPagination;
    
    int intPage;
    IBOutlet UIView *view_PopUp;
    IBOutlet UILabel *lblMessage;
}
- (IBAction)btnReferesh:(id)sender;

@end
