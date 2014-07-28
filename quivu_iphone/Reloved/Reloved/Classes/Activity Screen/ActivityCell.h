//
//  ActivityCell.h
//  Reloved
//
//  Created by GOURAV JOSHIE on 29/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellFunctionUserNameDelegate <NSObject>
- (void) btnUserNameClick: (int) indexValue;
@end

@interface ActivityCell : UITableViewCell

@property NSInteger getIndex;
@property (assign ,nonatomic) id<CellFunctionUserNameDelegate> delegate;
@property (strong, nonatomic) IBOutlet UIImageView *imgView_UserImage;
@property (strong, nonatomic) IBOutlet UIImageView *imgView_ProductImage;
@property (strong, nonatomic) IBOutlet UILabel *lbl_LastLogin;
@property (strong, nonatomic) IBOutlet UILabel *lbl_UserName_Msg;
@property (strong, nonatomic) IBOutlet UIImageView *imgIcon;
@property (strong, nonatomic) IBOutlet UIButton *btnUserName;
- (IBAction)btnUserName:(id)sender;
@property (strong, nonatomic) IBOutlet UIImageView *imgClockIcon;

@end
