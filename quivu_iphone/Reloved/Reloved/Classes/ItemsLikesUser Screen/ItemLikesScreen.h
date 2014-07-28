//
//  ItemLikesScreen.h
//  Reloved
//
//  Created by Kamal on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemsLikeCell.h"

@interface ItemLikesScreen : UIViewController <CellLikesUserDelegate,UITableViewDataSource,UITableViewDelegate>
{
    IBOutlet UITableView *tbl_LikesUser;
    IBOutlet UILabel *lblItemLikes;
    NSMutableArray * arrItemLikesUser;
}

@property (strong, nonatomic) NSString * ProductId;

- (IBAction)BtnReferesh:(id)sender;
- (IBAction)BtnBack:(id)sender;

@end
