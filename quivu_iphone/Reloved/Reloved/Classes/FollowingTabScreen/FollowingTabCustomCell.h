//
//  FollowingTabCustomCell.h
//  Reloved
//
//  Created by Kamal on 30/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellFunctionTabDelegate <NSObject>
- (void) FollowAndFollowingMethod: (int) indexValue;
@end

@interface FollowingTabCustomCell : UITableViewCell

@property NSInteger getIndex;

@property (assign ,nonatomic) id<CellFunctionTabDelegate> delegate;
@property (strong, nonatomic) IBOutlet UIImageView *img_ProfileImage;
@property (strong, nonatomic) IBOutlet UILabel *lbl_UserName;
@property (strong, nonatomic) IBOutlet UIButton *BtnFollow;

- (IBAction)BtnFollowingAction:(id)sender;

@end
