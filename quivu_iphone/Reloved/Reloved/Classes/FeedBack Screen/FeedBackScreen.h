//
//  FeedBackScreen.h
//  quivu
//
//  Created by Kamal on 04/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FeedBackCell.h"

@interface FeedBackScreen : UIViewController <UITableViewDataSource,UITableViewDelegate, UITextViewDelegate, cellFeedBackReplyDelegate>
{
    BOOL boolAll;
    BOOL boolAsSeller;
    BOOL boolAsBuyer;
    
    IBOutlet UIButton *BtnReferesh;
    IBOutlet UIButton *btnPlusIcon;
    
    IBOutlet UIButton * btn_All;
    IBOutlet UIButton * btn_AsSeller;
    IBOutlet UIButton * btn_AsBuyer;
    
    IBOutlet UIView *view_All;
    IBOutlet UIView *view_AsSeller;
    IBOutlet UIView *view_AsBuyer;
    
    IBOutlet UITableView *tbl_feedBack;
    NSMutableArray * arrAllFeedBackData;
    NSMutableArray * arrAsSeller;
    NSMutableArray * arrAsBuyer;

    IBOutlet UIView *view_FeedBackReply;
    IBOutlet UITextView *tv_FeedbackReply;
    IBOutlet UILabel *lblCounterReplyFeedBack;
    
    IBOutlet UIView *view_FeedBackEdit;
    IBOutlet UITextView *tv_FeedbackEdit;
    IBOutlet UILabel *lblCounterEditFeedBack;
}

@property (strong, nonatomic) NSMutableDictionary * dictUserInfo;

- (IBAction)BtnAll:(id)sender;
- (IBAction)BtnAsSeller:(id)sender;
- (IBAction)BtnAsBuyer:(id)sender;

- (IBAction)BtnReferesh:(id)sender;
- (IBAction)BtnBack:(id)sender;

- (IBAction)BtnRightReplyView:(id)sender;
- (IBAction)BtnBackReplyView:(id)sender;

- (IBAction)BtnRightEditView:(id)sender;
- (IBAction)BtnBackEditView:(id)sender;
- (IBAction)btnPlusIcon:(id)sender;

@end
