//
//  Following.h
//  Reloved
//
//  Created by Kamal on 23/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FollowingCustomCell.h"

@class FollowingCustomCell;
@interface Following : UIViewController <UITableViewDataSource,UITableViewDelegate,CellFunctionFollowingDelegate>
{
    IBOutlet UITableView *tbl_Follow;
    NSMutableArray * arrFollowingUser;
    BOOL boolPagination;
     NSString * ProfileId;
    
    int intPage;
}

@property (strong, nonatomic) NSMutableDictionary * DictProfileData;
- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnReferesh:(id)sender;
@end
