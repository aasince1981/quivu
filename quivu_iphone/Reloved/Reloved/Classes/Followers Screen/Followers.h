//
//  Followers.h
//  Reloved
//
//  Created by Kamal on 24/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableViewCell.h"

@class TableViewCell;
@interface Followers : UIViewController <UITableViewDelegate,UITableViewDataSource,CellFunctionFollowerDelegate>
{
  
    IBOutlet UITableView *tbl_Follower;
    NSMutableArray * arrFollower;
    BOOL boolPagination;
    int intPage;
    IBOutlet UILabel *lblFollowers;
}

@property (strong, nonatomic) NSMutableDictionary * DictProfileData;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnReferesh:(id)sender;

@end
