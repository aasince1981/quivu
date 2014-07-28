//
//  ActivityViewController.h
//  Reloved
//
//  Created by Kamal on 16/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ActivityCell.h"

@interface ActivityViewController : UIViewController<UITableViewDelegate,UITableViewDataSource,CellFunctionUserNameDelegate>
{
    NSString * strUserId;
    int intPage;
    BOOL  flagPagination;
    IBOutlet UITableView *tblView;
    
    NSString * ActivityTypeId;
    
    IBOutlet UIView * view_PopUp;
    IBOutlet UILabel * lblMessage;
}

@property (strong, nonatomic) NSMutableArray * arrActivities;
@property (strong, nonatomic) IBOutlet UIButton * btn_Search;

- (IBAction)btn_Refresh:(id)sender;
- (IBAction)btn_Search:(id)sender;

@end
