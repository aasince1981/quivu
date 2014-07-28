//
//  SearchUserCustomCell.h
//  quivu
//
//  Created by Kamal on 05/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CellSearchUserDelegate <NSObject>
- (void) FollowAndUnFollowUserSearch: (int) indexValue;
@end

@interface SearchUserCustomCell : UITableViewCell


@property NSInteger getIndex;
@property (strong, nonatomic) IBOutlet UIButton *BtnFollow;
@property (assign ,nonatomic) id<CellSearchUserDelegate> delegate;
@property (strong, nonatomic) IBOutlet UIImageView *img_ProfileImage;
@property (strong, nonatomic) IBOutlet UILabel *lbl_UserName;
- (IBAction)BtnFollowAction:(id)sender;

@end
