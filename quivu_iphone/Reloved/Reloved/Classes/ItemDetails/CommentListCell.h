//
//  CommentListCell.h
//  Reloved
//
//  Created by Kamal on 28/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommentListCell : UITableViewCell

@property (strong, nonatomic) IBOutlet UIImageView *imgUserImage;
@property (strong, nonatomic) IBOutlet UILabel *lblUserName;
@property (strong, nonatomic) IBOutlet UILabel *lblDuration;
@property (strong, nonatomic) IBOutlet UILabel *lblCommentMessage;
@end
