//
//  CommentScreen.m
//  Reloved
//
//  Created by Kamal on 27/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "CommentScreen.h"
#import "CommentCustomCell.h"

@interface CommentScreen ()
{
    NSString  * ReplyStaus;
    NSDictionary * dictCommentInfo;
    NSString  * ReplyUserName;
}

@end

@implementation CommentScreen
@synthesize ProductId,formItems,scrollView,enhancedKeyboard;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    tf_Message.delegate = self;
    [tf_Message setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];

    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_Message];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    scrollView.delegate = self;

    tbl_CommentList.dataSource = self;
    tbl_CommentList.delegate = self;
    arrCommentList = [[NSMutableArray alloc]init];
    
    dictCommentInfo = [[NSDictionary alloc]init];
    
    lblComments.font = FONT_Lato_Bold(20.0f);
    lblCommentOptions.font = FONT_Lato_Bold(20.0f);
    BtnDelete.titleLabel.font = FONT_Lato_Bold(16.0f);
    BtnReply.titleLabel.font = FONT_Lato_Bold(16.0f);
    ReplyStaus = @"0";
    ReplyUserName = @"";
    DictProductDetails = [[NSMutableDictionary alloc]init];
    
    boolAddComment = NO;
}

- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    [self DownloadItemDetailsData];
}

// View product webservice call here..........
- (void) DownloadItemDetailsData {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&ProductId=%@",[KAppDelegate getDictServer:WS_ViewProduct],ProductId];
        NSLog(@"ViewProduct Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            
            NSLog(@"ViewProduct Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    
                    DictProductDetails = [json objectForKey:@"ProductInformation"];
                    if(DictProductDetails.count > 0) {
                        arrCommentList = [DictProductDetails objectForKey:@"ProductCommentInfo"];
                        [tbl_CommentList reloadData];
                    }
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}
 
- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_Message.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
}

#pragma mark - TableView DataSource and Delegate Methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrCommentList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    CommentCustomCell *cell = (CommentCustomCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"CommentCustomCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrCommentList objectAtIndex:indexPath.row];
   
    cell.lblUserName.text = [dict objectForKey:@"CommentUserName"];
    NSString * CommentReplyStaus = [dict objectForKey:@"CommentReplyStaus"];

    if(CommentReplyStaus.intValue == 1) {
        cell.lblReplyUserName.hidden = NO;
        NSString * splitString = [dict objectForKey:@"CommentMessage"];
        NSArray *split = [splitString componentsSeparatedByString:@" "];
        cell.lblReplyUserName.text = [split objectAtIndex:0];
        
        NSString *lblUserName= [NSString stringWithFormat:@"%@",cell.lblReplyUserName.text];
        NSString *lblUserSms = [NSString stringWithFormat:@"%@",[dict objectForKey:@"CommentMessage"]];
        
        if(lblUserSms.length > 0) {
            cell.lblCommentMessage.text = [lblUserSms stringByReplacingOccurrencesOfString:lblUserName withString:@" "];
        }
        
    } else {
        cell.lblReplyUserName.hidden = YES;
        cell.lblCommentMessage.frame = CGRectMake(48,35,149, 25);
        cell.lblCommentMessage.text = [dict objectForKey:@"CommentMessage"];
    }
    
    cell.lblCommentMessage.lineBreakMode = NSLineBreakByWordWrapping;
    cell.lblCommentMessage.numberOfLines = 0;
    [cell.lblCommentMessage sizeToFit];
    cell.lblDuration.text = [NSString stringWithFormat:@"%@",[KAppDelegate getTimeDiffrece:[dict objectForKey:@"CommentTime"]]];
    
    @try {
        NSString * str =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"CommentUserImage"]];
        [obNet SetImageToView:cell.imgUserImage fromImageUrl:str Option:5];
    } @catch (NSException *exception) {}
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    dictCommentInfo = [arrCommentList objectAtIndex:indexPath.row];
    ReplyUserName = [NSString stringWithFormat:@"@%@",[dictCommentInfo objectForKey:@"CommentUserName"]];
    tableIndex = indexPath.row;
    view_PopUp.frame = self.view.frame;
    [self.view addSubview:view_PopUp];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSDictionary * dict = [arrCommentList objectAtIndex:indexPath.row];
    CGSize size =   [self sizeOfText:[dict objectForKey:@"CommentMessage"] widthOfLabel:149 withFont:FONT_Lato_Bold(12.0f)];
    UILabel * lbl = [[UILabel alloc]init];
    lbl.text = [dict objectForKey:@"CommentMessage"];
    
    return 45.0+size.height;
}

-(CGSize)sizeOfText:(NSString *)textToMesure widthOfLabel:(CGFloat)width withFont:(UIFont*)font
{
    CGSize ts = [textToMesure sizeWithFont:font constrainedToSize:CGSizeMake(width-20.0, FLT_MAX) lineBreakMode:NSLineBreakByWordWrapping];
    return ts;
}

#pragma mark - Button Actions

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

// button add comment......
- (IBAction)BtnAddComment:(id)sender {
    [self AddComment];
}

// button add Reply......
- (IBAction)BtnReply:(id)sender {
    boolAddComment = YES;
    [tf_Message becomeFirstResponder];
    tf_Message.text = [NSString stringWithFormat:@"%@",ReplyUserName];
    [view_PopUp removeFromSuperview];
}

// button add Delete comment......
- (IBAction)BtnDelete:(id)sender {
    NSString * CommentId = @"";
    
    if(dictCommentInfo.count > 0) {
        @try {
            CommentId = [NSString stringWithFormat:@"%d",[[dictCommentInfo objectForKey:@"CommentId"] intValue]];
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&CommentId=%@",[KAppDelegate getDictServer:WS_DeleteComment],CommentId];
        NSLog(@"DeleteComment Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"DeleteComment Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [view_PopUp removeFromSuperview];
                    [self DownloadItemDetailsData];
                } else {
                    [view_PopUp removeFromSuperview];
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

- (IBAction)BtnReferesh:(id)sender {
    tf_Message.text = @"";
    [self DownloadItemDetailsData];
}

- (IBAction)btnRemovePopUpView:(id)sender {
    [view_PopUp removeFromSuperview];
}

// here we addComment webservice...........
- (void) AddComment {
    [tf_Message resignFirstResponder];
    if(IS_IPHONE_5) {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,518, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    } else {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,430, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    }
   [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    
    NSString  * FromUserId = @"";
    NSString  * FromUserName = @"";
    NSString  * FromUserImage = @"";
    NSString  * ToUserId = @"";
    NSString  * ToUserName = @"";
    NSString  * ProductName = @"";
    NSString  * ProductImage = @"";
    NSString  * OwnerStatus = @"";
    
    if(KAppDelegate.dictLoginInfo.count > 0){
        @try {
            FromUserId = [NSString stringWithFormat:@"%d",[[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] intValue]];
        } @catch (NSException *exception) { }
        
        @try {
            FromUserName = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
        } @catch (NSException *exception) { }
        
        @try {
            if([KAppDelegate.dictLoginInfo objectForKey:@"UserImage"] != nil) {
                FromUserImage = [KAppDelegate.dictLoginInfo objectForKey:@"UserImage"];
            } else {
                FromUserImage = @"";
            }
        } @catch (NSException *exception) { }
    }
    
    if(DictProductDetails.count > 0){
        @try {
            ToUserId = [DictProductDetails objectForKey:@"ProductUserId"];
        } @catch (NSException *exception) { }
        
        @try {
            ProductName = [DictProductDetails objectForKey:@"ProductUserName"];
        } @catch (NSException *exception) { }
        
        @try {
            ProductImage = [DictProductDetails objectForKey:@"ProductUserImage"];
        } @catch (NSException *exception) { }
        
        @try {
            ToUserName =[NSString stringWithFormat:@"%@",[DictProductDetails objectForKey:@"ProductUserName"]];
        } @catch (NSException *exception) { }
    }
    
    if(FromUserId.intValue == ToUserId.intValue) {
        OwnerStatus = @"1";
    } else {
        OwnerStatus = @"0";
    }
    
    if(boolAddComment) {
        ReplyStaus = @"1";
    } else {
        ReplyStaus = @"0";
    }
    
    if(tf_Message.text.length == 0) {
        [obNet PopUpMSG:@"Please enter comment." Header:@""];
    } else {
        if([obNet InternetStatus:YES]) {
            NSString *url = [NSString stringWithFormat:@"%@&CommentProductId=%@&CommentUserId=%@&CommentUserName=%@&CommentUserImage=%@&CommentMessage=%@&CommentProductImage=%@&CommentProductName=%@&CommentToUserId=%@&CommentToUserName=%@&OwnerStatus=%@&ReplyStaus=%@&ReplyToUserId=%@&ReplyToUserName=%@",[KAppDelegate getDictServer:WS_Addcomment],ProductId,FromUserId,FromUserName,FromUserImage,tf_Message.text,ProductImage,ProductName,ToUserId,ToUserName,OwnerStatus,ReplyStaus,ToUserId,ToUserName];
            NSLog(@"Addcomment Url-->%@",url);
            
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
                NSLog(@"Addcomment Json value-->%@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        
                        boolAddComment = NO;
                        [view_PopUp removeFromSuperview];
                        tf_Message.text = @"";
                        [tf_Message resignFirstResponder];
                        
                        @try {
                            NSDictionary * dict = [json objectForKey:@"Comment"];
                            NSMutableArray *  tempArr = [arrCommentList mutableCopy];
                            [tempArr insertObject:dict atIndex:0];
                            //[tempArr addObject:dict];
                            arrCommentList = tempArr;
                        } @catch (NSException *exception) {}
                        
                        [tbl_CommentList reloadData];
                    } else {
                        boolAddComment = NO;
                        [tf_Message resignFirstResponder];
                        UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                        [alert show];
                    }
                }
            }];
        }
    }
}

#pragma mark == textfield delegate methods
- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    [textField resignFirstResponder];
    [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    
    if(IS_IPHONE_5) {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,518, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    } else {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,430, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    }
    
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    if(IS_IPHONE_5) {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,307 , view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    } else {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,218 , view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    }
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol
- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        if(tf == tf_Message){
            [tf_Message resignFirstResponder];
            [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
        }
        
        if ([tf isEditing] && i!=[self.formItems count]-1) {
            UITextField * tf1 = (UITextField *)[self.formItems objectAtIndex:i+1];
            [tf1 becomeFirstResponder];
            break;
        }
    }
}

- (void)previousDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        
        if(tf == tf_Message){
            [tf_Message resignFirstResponder];
            [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
        }
        
        if ([tf isEditing] && i!=[self.formItems count]+1 && i != 0)
        {
            UITextField * tf1 = (UITextField *)[self.formItems objectAtIndex:i-1];
            [tf1 becomeFirstResponder];
            break;
        }
    }
}

- (void)doneDidTouchDown
{
    for(UITextField * tf in self.formItems)
    {
        if ([tf isEditing])
        {
            [tf resignFirstResponder];
            [scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
            break;
        }
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
