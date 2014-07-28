//
//  ItemsLikeCell.h
//  Reloved
//
//  Created by Kamal on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellLikesUserDelegate <NSObject>
- (void) FollowAndFollowingLikeUser: (int) indexValue;
@end

@interface ItemsLikeCell : UITableViewCell

@property NSInteger getIndex;
@property (strong, nonatomic) IBOutlet UIButton *BtnFollow;
@property (assign ,nonatomic) id <CellLikesUserDelegate> delegate;
@property (strong, nonatomic) IBOutlet UIImageView *img_ProfileImage;
@property (strong, nonatomic) IBOutlet UILabel *lbl_UserName;

- (IBAction)BtnFollowAction:(id)sender;

@end
