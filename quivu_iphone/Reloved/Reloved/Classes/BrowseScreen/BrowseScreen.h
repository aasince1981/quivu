//
//  BrowseScreen.h
//  Reloved
//
//  Created by Kamal on 15/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LeveyTabBarController;
@interface BrowseScreen : UIViewController <LeveyTabBarControllerDelegate>
{
    IBOutlet UIScrollView * scrollView;
    NSMutableArray * arrCategoryList;
    
    IBOutlet UIView * view_CategoryList;
    IBOutlet UIButton * btnCategoryView;
    IBOutlet UIImageView *imgCategoryView;
    IBOutlet UILabel *lblCategoryName;
    IBOutlet UIImageView *imgTextBg;
    
    IBOutlet UIView *view_PopUp;
    IBOutlet UILabel *lblMessage;
}

- (IBAction)btnUserPlusIcon:(id)sender;
- (IBAction)btnSearchIcon:(id)sender;
- (IBAction)btnReferesh:(id)sender;

- (void) viewWillAppearClone;

@end
