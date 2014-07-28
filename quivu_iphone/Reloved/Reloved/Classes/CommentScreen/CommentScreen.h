//
//  CommentScreen.h
//  Reloved
//
//  Created by Kamal on 27/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KSEnhancedKeyboard.h"

@interface CommentScreen : UIViewController <KSEnhancedKeyboardDelegate,UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,UIScrollViewDelegate>
{
    IBOutlet UITableView *tbl_CommentList;
    NSMutableArray * arrCommentList;
    IBOutlet UITextField *tf_Message;
    IBOutlet UILabel *lblComments;
    NSInteger tableIndex;
    
    IBOutlet UIView *view_PopUp;
    IBOutlet UIButton *BtnReply;
    IBOutlet UIButton *BtnDelete;
    IBOutlet UILabel *lblCommentOptions;
    NSMutableDictionary * DictProductDetails;
    IBOutlet UIView * view_AddMessage;
    
    BOOL boolAddComment;
}

@property (strong, nonatomic) NSString * ProductId;
@property (strong, nonatomic) NSMutableArray *formItems;
@property (strong,nonatomic) IBOutlet UIScrollView *scrollView;
@property  (strong,nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

- (IBAction)BtnBack:(id)sender;
- (IBAction)BtnAddComment:(id)sender;
- (IBAction)BtnReply:(id)sender;
- (IBAction)BtnDelete:(id)sender;
- (IBAction)BtnReferesh:(id)sender;
- (IBAction)btnRemovePopUpView:(id)sender;

@end
