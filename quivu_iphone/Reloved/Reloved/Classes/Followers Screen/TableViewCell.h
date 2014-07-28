//
//  TableViewCell.h
//  Reloved
//
//  Created by Kamal on 24/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellFunctionFollowerDelegate <NSObject>
- (void) FollowerAndUnFollower: (int) indexValue;
@end

@interface TableViewCell : UITableViewCell

@property NSInteger getIndex;
@property (strong, nonatomic) IBOutlet UIButton *BtnFollow;
@property (assign ,nonatomic) id<CellFunctionFollowerDelegate> delegate;
@property (strong, nonatomic) IBOutlet UIImageView *img_ProfileImage;
@property (strong, nonatomic) IBOutlet UILabel *lbl_UserName;
- (IBAction)BtnFollowAction:(id)sender;

@end
