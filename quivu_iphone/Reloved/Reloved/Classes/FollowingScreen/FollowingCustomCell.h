//
//  FollowingCustomCell.h
//  Reloved
//
//  Created by Kamal on 23/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellFunctionFollowingDelegate <NSObject>
- (void) FollowAndUnFollow: (int) indexValue;
@end

@interface FollowingCustomCell : UITableViewCell

@property int getIndex;
@property (assign ,nonatomic) id<CellFunctionFollowingDelegate> delegate;
@property (strong, nonatomic) IBOutlet UIImageView *img_ProfileImage;
@property (strong, nonatomic) IBOutlet UILabel *lbl_UserName;
@property (strong, nonatomic) IBOutlet UIButton *BtnFollow;

- (IBAction)BtnFollowingAction:(id)sender;
@end
