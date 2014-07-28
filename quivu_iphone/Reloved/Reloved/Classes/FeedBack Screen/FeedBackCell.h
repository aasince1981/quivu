//
//  FeedBackCell.h
//  quivu
//
//  Created by Kamal on 04/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol cellFeedBackReplyDelegate <NSObject>
- (void) FeedBackReplyButtonArrow: (int) index;
- (void) FeedBackEditIconButton: (int) index;
@end

@interface FeedBackCell : UITableViewCell
@property (strong, nonatomic) IBOutlet UIImageView *imgUserImage;
@property (strong, nonatomic) IBOutlet UILabel *lblUserName;
@property (strong, nonatomic) IBOutlet UILabel *lblMessage;
@property (strong, nonatomic) IBOutlet UIImageView *imgIcon;
@property (strong, nonatomic) IBOutlet UILabel *lblFeedBackTime;
@property (strong, nonatomic) IBOutlet UIButton *btnFeedBackImage;
@property (strong, nonatomic) IBOutlet UIButton *btnEditIconImage;
@property NSInteger getIndex;
@property (assign ,nonatomic) id<cellFeedBackReplyDelegate> delegate;
@property (strong, nonatomic) IBOutlet UILabel *lblFeedBackReplyTime;
@property (strong, nonatomic) IBOutlet UILabel *lblReplyUserName;
@property (strong, nonatomic) IBOutlet UILabel *lblFeedBackReplyMessage;
@property (strong, nonatomic) IBOutlet UIImageView *imgClockFeedBackTime;
@property (strong, nonatomic) IBOutlet UIImageView *imgClockReplyTime;
@property (strong, nonatomic) IBOutlet UIButton *btnArrowRight;

- (IBAction)BtnFeedBackReply:(id)sender;
- (IBAction)btnEditIconImage:(id)sender;

@end
