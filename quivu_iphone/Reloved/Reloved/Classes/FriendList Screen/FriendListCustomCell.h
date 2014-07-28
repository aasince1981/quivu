//
//  FriendListCustomCell.h
//  quivu
//
//  Created by Kamal on 12/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellFriendFollowDelegate <NSObject>
- (void) FollowAndUnFollowFriends: (int) indexValue;
@end

@interface FriendListCustomCell : UITableViewCell

@property NSInteger getIndex;
@property (assign ,nonatomic) id<CellFriendFollowDelegate> delegate;
@property (strong, nonatomic) IBOutlet UIImageView *img_ImageView;
@property (strong, nonatomic) IBOutlet UILabel *lblName;
@property (strong, nonatomic) IBOutlet UIButton *btnFollow;
- (IBAction)btnFollow:(id)sender;

@end
