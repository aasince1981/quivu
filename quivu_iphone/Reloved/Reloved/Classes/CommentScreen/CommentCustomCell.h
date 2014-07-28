//
//  CommentCustomCell.h
//  Reloved
//
//  Created by Kamal on 27/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommentCustomCell : UITableViewCell
@property (strong, nonatomic) IBOutlet UIImageView *imgUserImage;
@property (strong, nonatomic) IBOutlet UILabel *lblUserName;
@property (strong, nonatomic) IBOutlet UILabel *lblDuration;
@property (strong, nonatomic) IBOutlet UILabel *lblCommentMessage;
@property (strong, nonatomic) IBOutlet UILabel *lblReplyUserName;

@end
