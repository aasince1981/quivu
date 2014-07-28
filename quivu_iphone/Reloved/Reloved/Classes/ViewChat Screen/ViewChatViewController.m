//
//  ViewChatViewController.m
//  Reloved
//
//  Created by Kamal on 27/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ViewChatViewController.h"
#import "ViewChatCustomCell.h"
#import "ItemDetails.h"

@interface ViewChatViewController ()

@end

@implementation ViewChatViewController
@synthesize ProductId,boolCancelOffer,formItems,enhancedKeyboard,dictOfferDetails;

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
    sv_ScrollView.delegate = self;
    
    tf_Message.delegate = self;
    [tf_Message setValue:[UIColor darkGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_Message];
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    arrCommentList = [[NSMutableArray alloc]init];
    tbl_CommentList.dataSource = self;
    tbl_CommentList.delegate = self;
    
    DictProductDetails = [[NSMutableDictionary alloc]init];
    MessageType = @"0";
    strImgMessage = @"";
    [self SetFontType];
    [self performSelectorInBackground:@selector(DownloadItemDetailsData) withObject:nil];
}

// here we get font type and size......................
- (void) SetFontType {
    lblViewChat.font = FONT_Lato_Bold(20.0f);
    lblProductName.font = FONT_Lato_Bold(17.0f);
    lblProductPrice.font = FONT_Lato_Bold(17.0f);
    lblPriceOfferedText.font = FONT_Lato_Bold(17.0f);
    lblPriceOffered.font = FONT_Lato_Bold(15.0f);
    lblProductUserName.font = FONT_Lato_Bold(12.0f);
    lblProductOfferPrice.font = FONT_Lato_Light(12.0f);
    lblTimeDuration.font = FONT_Lato_Light(12.0f);
    BtnCancelOffer.titleLabel.font = FONT_Lato_Bold(15.0f);
    lblCancelOfferPopUp.font = FONT_Lato_Bold(17.0f);
    BtnNo.titleLabel.font = FONT_Lato_Bold(17.0f);
    BtnYes.titleLabel.font = FONT_Lato_Bold(17.0f);
    lblAlertText.font = FONT_Lato_Light(17.0f);
    lblDealLocation.font = FONT_Lato_Bold(17.0f);
    lblDealLocationName.font = FONT_Lato_Bold(15.0f);
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
}

// here we set value to view........................
- (void) SetValueToview {
    if(DictProductDetails.count > 0) {
        
        NSArray * arr = [DictProductDetails objectForKey:@"ProductImageInfo"];
        @try {
            if(arr.count > 0) {
                for (int i =0; i < arr.count; i++) {
                    NSString * str =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arr objectAtIndex:i] objectForKey:@"ProImageName"]];
                    [obNet SetImageToView:imgProduct fromImageUrl:str Option:5];
                }
            }
        } @catch (NSException *exception) {}
        
        @try {
            lblProductName.text = [NSString stringWithFormat:@"%@",[DictProductDetails objectForKey:@"ProductName"]];
        } @catch (NSException *exception) {}
        
        @try {
            lblProductPrice.text = [NSString stringWithFormat:@"$ %@",[DictProductDetails objectForKey:@"ProductPrice"]];
        } @catch (NSException *exception) {}
        
        @try {
            NSArray * arrr = [DictProductDetails objectForKey:@"ProductOfferInfo"];
            if(arrr.count > 0) {
                for(int i = 0; i< arrr.count ; i++) {
                    lblPriceOffered.text = [NSString stringWithFormat:@"$ %@",[[arrr objectAtIndex:0] objectForKey:@"Amount"]];
                    lblProductOfferPrice.text = [NSString stringWithFormat:@"offered $%@",[[arrr objectAtIndex:0] objectForKey:@"Amount"]];
                }
            }
        } @catch (NSException *exception) {}
        
        @try {
            lblProductUserName.text = [NSString stringWithFormat:@"%@",[DictProductDetails objectForKey:@"ProductUserName"]];
        } @catch (NSException *exception) {}
        
        @try {
            lblDealLocationName.text = [DictProductDetails objectForKey:@"ProductAddress"];
        } @catch (NSException *exception) {}
        
        @try {
            lblTimeDuration.text = [NSString stringWithFormat:@"%@",[KAppDelegate getTimeDiffrece:[DictProductDetails objectForKey:@"ProductAddDate"]]];
        } @catch (NSException *exception) {}
        
        @try {
            if(dictOfferDetails.count > 0) {
                if(boolCancelOffer) {
                    lblPriceOffered.text = [NSString stringWithFormat:@"$ %@",[dictOfferDetails objectForKey:@"OfferAmount"]];
                    lblProductOfferPrice.text = [NSString stringWithFormat:@"offered $%@",[dictOfferDetails objectForKey:@"OfferAmount"]];
                }
                
                if(boolCancelOffer) {
                    lblProductUserName.text = [dictOfferDetails objectForKey:@"OfferUserName"];
                    NSString * str = [NSString stringWithFormat:@"Mark as sold to %@",lblProductUserName.text];
                    [BtnCancelOffer setTitle:[NSString stringWithFormat:@"%@",str] forState:UIControlStateNormal];
                }
            }
        } @catch (NSException *exception) {}
        
        @try {
            NSString * strImageUrl =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[DictProductDetails objectForKey:@"ProductUserImage"]];
            [obNet SetImageToView:imgProductUserImage fromImageUrl:strImageUrl Option:IV_Save];
            
        } @catch (NSException *exception) {}
    }
}

#pragma mark - GetData From Webservices
// here get view product data...........................
- (void) DownloadItemDetailsData {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&ProductId=%@",[KAppDelegate getDictServer:WS_ViewProduct],ProductId];
        NSLog(@"ViewProduct Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"ViewProduct Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    DictProductDetails = [json objectForKey:@"ProductInformation"];
                    [self SetValueToview];
                    [self GetMessages];
                } else {
                    [obNet PopUpMSG:[json objectForKey:@"msg"] Header:@""];
                }
            }
        }];
    }
}

// here we call getMessage webservice......................
- (void) GetMessages {
    
    NSString  * FromUserId = @"";
    NSString  * ToUserId = @"";
    
    if(KAppDelegate.dictLoginInfo.count > 0){
        @try {
            FromUserId = [NSString stringWithFormat:@"%d",[[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] intValue]];
        } @catch (NSException *exception) { }
    }
    
    if(DictProductDetails.count > 0){
        @try {
            ToUserId = [DictProductDetails objectForKey:@"ProductUserId"];
        } @catch (NSException *exception) { }
    }
    
    if(boolCancelOffer) {
        if(dictOfferDetails.count >0) {
            ToUserId = [dictOfferDetails objectForKey:@"OfferUserId"];
        }
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&ToUserId=%@&MessageProductId=%@",[KAppDelegate getDictServer:WS_GetMessages],FromUserId,ToUserId,ProductId];
        NSLog(@"GetMessages Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetMessages Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrCommentList = [NSMutableArray arrayWithArray:[json objectForKey:@"Messages"]];
                    [tbl_CommentList reloadData];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    //[alert show];
                }
            }
        }];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - Button Actions

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)BtnReferesh:(id)sender {
    [self DownloadItemDetailsData];
}

- (IBAction)BtnCancelOffer:(id)sender {
    
    if(boolCancelOffer) {
        [self MarkAsSoldData];
    } else {
        view_PopUp.frame = self.view.frame;
        [self.view addSubview:view_PopUp];
    }
}

// here we call on  mark as Sold webservice..............
- (void) MarkAsSoldData {
    NSString * ProductOfferId ;
    NSString * ProductName ;
    NSString * ProductImage ;
    NSString * FromUserId;
    NSString * FromUserName;
    NSString * FromUserImage;
    
    if(KAppDelegate.dictLoginInfo) {
        
        @try {
            FromUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) {}
        
        @try {
            FromUserName = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
        } @catch (NSException *exception) {}
        
        @try {
            FromUserImage = [KAppDelegate.dictLoginInfo objectForKey:@"UserImage"];
        } @catch (NSException *exception) {}
    }
    
    if(dictOfferDetails.count > 0) {
        @try {
            ProductOfferId = [dictOfferDetails objectForKey:@"OfferProductId"];
        } @catch (NSException *exception) {}
        
        @try {
            ProductName = [dictOfferDetails objectForKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            ProductImage = [dictOfferDetails objectForKey:@"ProductImage"];
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&ProductId=%@&ProductName=%@&ProductImage=%@&FromUserId=%@&FromUserName=%@&FromUserImage=%@",[KAppDelegate getDictServer:WS_MarkAsSold],ProductId,ProductName,ProductImage,FromUserId,FromUserName,FromUserImage];
        NSLog(@"MarkAsSold Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"MarkAsSold Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [self.navigationController popViewControllerAnimated:YES];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

// here we call cancel offer webservice..........................
- (IBAction)BtnYes:(id)sender {
    NSString  * FromUserId = @"";
    NSString  * FromUserName = @"";
    NSString  * FromUserImage = @"";
    NSString  * ToUserId = @"";
    NSString  * ProductName = @"";
    NSString  * ProductImage = @"";
    NSString  * status = @"1";
    
    if(KAppDelegate.dictLoginInfo.count > 0){
        @try {
            if(boolCancelOffer) {
                FromUserId = [dictOfferDetails objectForKey:@"OfferUserId"];
            } else {
                FromUserId = [NSString stringWithFormat:@"%d",[[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] intValue]];
            }
            
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
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&CancelFromUserId=%@&CancelFromUserName=%@&CancelFromUserImage=%@&CancelToUserId=%@&CancelStatus=%@&CancelProductId=%@&CancelProductName=%@&CancelProductImage=%@&OfferCancelBy=%@",[KAppDelegate getDictServer:WS_CancelOffer],FromUserId,FromUserName,FromUserImage,ToUserId,status,ProductId,ProductName,ProductImage,FromUserId];
        NSLog(@"CancelOffer Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"CancelOffer Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [view_PopUp removeFromSuperview];
                    
                    static NSInteger dir = 0;
                    ItemDetails * vc = [[ItemDetails alloc]initWithNibName:@"ItemDetails" bundle:nil];
                    dir++;
                    
                    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
                    
                    if(ProductId != nil) {
                        vc.ProductId = [NSString stringWithFormat:@"%@",ProductId];
                    }
                    
                    /*
                    NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
                    if(ProductId != nil) {
                        [dd setObject:ProductId forKey:@"ProductId"];
                    }
                    
                    if(ToUserId != nil) {
                        [dd setObject:ToUserId forKey:@"ProductUserId"];
                    }
                    
                    if(dd.count > 0) {
                        vc.dictProductId = dd;
                    }
                     */
                    
                    vc.hidesBottomBarWhenPushed = YES;
                    [self.navigationController pushViewController:vc animated:NO];
                    
                } else {
                    [view_PopUp removeFromSuperview];
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

- (IBAction)BtnNo:(id)sender {
    [view_PopUp removeFromSuperview];
}

- (IBAction)BtnPlusIcon:(id)sender {
    MessageType = @"1";
    
    UIActionSheet * actionSheet = [[UIActionSheet alloc]initWithTitle:@"" delegate:self cancelButtonTitle:@"Cancel" destructiveButtonTitle:@"From Camera" otherButtonTitles:@"From Gallery",nil];
    [actionSheet showInView:self.view];
    //Message = [NSString stringWithFormat:@"%@",strImage];
}

// here we call addMessage webservice.................
- (IBAction)BtnAddMessage:(id)sender {
     MessageType = @"0";
    if([tf_Message.text isEqualToString:@""]) {
        [[[UIAlertView alloc] initWithTitle:@"" message:@"Please enter comment" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
    } else {
        [tf_Message resignFirstResponder];
        if(IS_IPHONE_5) {
            view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,526, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
        } else {
            view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,438, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
        }
        
        NSString  * FromUserId = @"";
        NSString  * FromUserName = @"";
        NSString  * FromUserImage = @"";
        NSString  * ToUserId = @"";
         NSString  * productImage = @"";
        
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
            NSArray * arr = [DictProductDetails objectForKey:@"ProductImageInfo"];
            @try {
                if(arr.count > 0) {
                    for (int i =0; i < arr.count; i++) {
                        productImage = [[arr objectAtIndex:i] objectForKey:@"ProImageName"];
                    }
                }
            } @catch (NSException *exception) {}
            
            @try {
                ToUserId = [DictProductDetails objectForKey:@"ProductUserId"];
            } @catch (NSException *exception) { }
        }
        
        if(boolCancelOffer) {
            if(dictOfferDetails.count >0) {
                ToUserId = [dictOfferDetails objectForKey:@"OfferUserId"];
            }
        }
        
        if([obNet InternetStatus:YES]) {
            NSString *url = [NSString stringWithFormat:@"%@&MessageFromUserId=%@&MessageFromUserName=%@&MessageFromUserImage=%@&MessageToUserId=%@&Message=%@&MessageType=%@&MessageProductId=%@&MessageProductImage=%@",[KAppDelegate getDictServer:WS_AddMessages],FromUserId,FromUserName,FromUserImage,ToUserId,tf_Message.text,MessageType,ProductId,productImage];
            NSLog(@"addMessages Url-->%@",url);
            
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
                NSLog(@"addMessages Json value-->%@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        
                        @try {
                            NSDictionary * dict = [json objectForKey:@"message"];
                            if (!arrCommentList)
                                arrCommentList = [NSMutableArray new];
                            //[arrCommentList addObject:dict];
                            [arrCommentList insertObject:dict atIndex:0];
                            tf_Message.text = @"";
                            [tbl_CommentList reloadData];
                        } @catch (NSException *exception) {}
                        
                    } else {
                        UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                        [alert show];
                    }
                }
            }];
        }
    }
}

#pragma mark - tableView DataSource and Delegate methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrCommentList.count;
}

- (UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    ViewChatCustomCell *cell = (ViewChatCustomCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"ViewChatCustomCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrCommentList objectAtIndex:indexPath.row];
    
    cell.lblUserName.text = [NSString stringWithFormat:@"%@",[dict objectForKey:@"MessageFromUserName"]];
    cell.lblDuration.text = [KAppDelegate getTimeDiffrece:[dict objectForKey:@"MessageAddTime"]];
    
    NSString * strMesage = [NSString stringWithFormat:@"%@",[dict objectForKey:@"Message"]];
    NSString *extension = [strMesage pathExtension];
    
    BOOL isJpegImage =(([extension caseInsensitiveCompare:@"jpg"] == NSOrderedSame) || ([extension caseInsensitiveCompare:@"jpeg"] == NSOrderedSame));
    
    if (isJpegImage) {
        cell.lblCommentMessage.hidden = YES;
        cell.imgPickImage.hidden = NO;
        
        if([obNet isObject:[dict objectForKey:@"Message"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            @try {
                NSString * str = [NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"messageImage"],[dict objectForKey:@"Message"]];
                [cell.imgPickImage GetNSetUIImage:str DefaultImage:@""];
                //[obNet SetImageToView:cell.imgPickImage fromImageUrl:str Option:5];
            } @catch (NSException *exception) {}
        }
    } else {
        cell.lblCommentMessage.hidden = NO;
        cell.imgPickImage.hidden = YES;
        cell.lblCommentMessage.text = [dict objectForKey:@"Message"];
        cell.lblCommentMessage.lineBreakMode = NSLineBreakByWordWrapping;
        cell.lblCommentMessage.numberOfLines = 0;
        [cell.lblCommentMessage sizeToFit];
        
        //CGSize size = [self sizeOfText:[dict objectForKey:@"Message"] widthOfLabel:149 withFont:FONT_Lato_Bold(12.0f)];
        //CGRect rect = cell.lblCommentMessage.frame;
       // rect.size.height = size.height;
        //cell.lblCommentMessage.frame   = rect;
    }
    
    @try {
        NSString * str =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"MessageFromUserImage"]];
        [cell.imgUserImage GetNSetUIImage:str DefaultImage:@""];
        //[obNet SetImageToView:cell.imgUserImage fromImageUrl:str Option:5];
    } @catch (NSException *exception) {}
    
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary * dict = [arrCommentList objectAtIndex:indexPath.row];
    CGFloat height;
    
    CGSize size = [self sizeOfText:[dict objectForKey:@"Message"] widthOfLabel:149 withFont:FONT_Lato_Bold(12.0f)];
    UILabel * lbl = [[UILabel alloc]init];
    NSString * strMesage = [dict objectForKey:@"Message"];
    
    NSString *extension = [strMesage pathExtension];
    BOOL isJpegImage =(([extension caseInsensitiveCompare:@"jpg"] == NSOrderedSame) || ([extension caseInsensitiveCompare:@"jpeg"] == NSOrderedSame));
    
    if(isJpegImage) {
        height = 45;
    } else {
        height = 0;
        lbl.text = [dict objectForKey:@"Message"];
    }

    return 60.0 + size.height + height;
}

-(CGSize)sizeOfText:(NSString *)textToMesure widthOfLabel:(CGFloat)width withFont:(UIFont*)font
{
    CGSize ts = [textToMesure sizeWithFont:font constrainedToSize:CGSizeMake(width-20.0, FLT_MAX) lineBreakMode:NSLineBreakByWordWrapping];
    return ts;
}

#pragma mark == textfield delegate methods
- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    //[textField resignFirstResponder];
    //[sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    
    [tf_Message resignFirstResponder];
    if(IS_IPHONE_5) {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,526, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    } else {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,438, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    }
    
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    if(IS_IPHONE_5) {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,310, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    } else {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,223 , view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
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
            [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
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
            [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
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
            [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
            break;
        }
    }
}

#pragma mark - Image Uplaod Code here

- (void) actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(buttonIndex == 0){
        [self imageUploadFromCamera];
    } else if (buttonIndex == 1){
        [self imageUploadFromGallery];
    }
}

- (void) imageUploadFromGallery {
    if (!imgPkr) {
        imgPkr=[[UIImagePickerController alloc]init];
        imgPkr.delegate=(id)self;
    }
    
    if ([UIImagePickerController isSourceTypeAvailable: UIImagePickerControllerSourceTypePhotoLibrary]) {
        imgPkr.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    }
    
    imgPkr.allowsEditing=YES;
    [self presentViewController:imgPkr animated:YES completion:^{
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissModalViewControllerAnimated:YES];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    myImage = info[UIImagePickerControllerEditedImage];
    [self AddMessageWithImageUpload];
    [picker dismissModalViewControllerAnimated:NO];
    [popover dismissPopoverAnimated:NO];
}

// here we call addMessage with upload image to server..................
- (void) AddMessageWithImageUpload {
    //[tf_Message resignFirstResponder];
    //[sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    
    [tf_Message resignFirstResponder];
    if(IS_IPHONE_5) {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,526, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    } else {
        view_AddMessage.frame = CGRectMake(view_AddMessage.frame.origin.x,438, view_AddMessage.frame.size.width, view_AddMessage.frame.size.height);
    }
    
    NSString  * FromUserId = @"";
    NSString  * FromUserName = @"";
    NSString  * FromUserImage = @"";
    NSString  * ToUserId = @"";
    NSString  * ProductImage = @"";
    
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
        
        if(DictProductDetails.count > 0){
            NSArray * arr = [DictProductDetails objectForKey:@"ProductImageInfo"];
            @try {
                if(arr.count > 0) {
                    for (int i =0; i < arr.count; i++) {
                        ProductImage = [[arr objectAtIndex:i] objectForKey:@"ProImageName"];
                    }
                }
            } @catch (NSException *exception) {}
        }
    }
    
    if(boolCancelOffer) {
        if(dictOfferDetails.count >0) {
            ToUserId = [dictOfferDetails objectForKey:@"OfferUserId"];
        }
    }
    
    if([obNet InternetStatus:YES]) {
        
        NSString *webservice = [NSString stringWithFormat:@"%@&MessageFromUserId=%@&MessageFromUserName=%@&MessageFromUserImage=%@&MessageToUserId=%@&MessageType=%@&MessageProductId=%@&MessageProductImage=%@",[KAppDelegate getDictServer:WS_AddMessages],FromUserId,FromUserName,FromUserImage,ToUserId,MessageType,ProductId,ProductImage];
        NSLog(@"addMessages webservice-->%@",webservice);
        
        //NSLog(@"imgProfile1-%@",myImage);
        NSData *fileData1 = UIImageJPEGRepresentation(myImage, 0.2);
        NSLog(@"UploadFileWithFileData-%lu-%@", (unsigned long)fileData1.length, webservice);
        
        webservice = [webservice stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        NSLog(@"UploadFileWithFileData-%lu-%@", (unsigned long)fileData1.length, webservice);
        
        NSURL *url = [NSURL URLWithString:webservice];
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc]initWithURL:url] ;
        request.HTTPMethod = @"POST";
        
        NSString *boundary = @"0xKhTmLbOuNdArY";
        NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", boundary, nil];
        [request addValue:contentType forHTTPHeaderField:@"Content-Type"];
        
        NSMutableData *body = [NSMutableData data];
        
        if (fileData1) {
            [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
            [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"Message\"; filename=\"%@\"\r\n", @"hhh.jpg"] dataUsingEncoding:NSUTF8StringEncoding]];
            [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
            [body appendData:fileData1];
            [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        }
        
        [request setHTTPBody:body];
        
        @try {
            NSURLResponse *response;
            NSData *POSTReply = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
            NSString *theReply = [[NSString alloc] initWithBytes:[POSTReply bytes] length:[POSTReply length] encoding: NSASCIIStringEncoding];
            NSLog(@"theReply-%@", theReply);
            
            NSError * e;
            NSMutableDictionary *dictJSON = [NSJSONSerialization JSONObjectWithData: [theReply dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers error: &e];
            NSLog(@"addMessages Url-->%@",webservice);
            NSLog(@"UploadFileWithFileDataJSON = %@, e-%@", dictJSON, e);
            
            if (dictJSON != nil) {
                if([[dictJSON objectForKey:@"success"] intValue] == 1) {
                    @try {
                        NSDictionary * dict = [dictJSON objectForKey:@"message"];
                        if (!arrCommentList)
                            arrCommentList = [NSMutableArray new];
                        //[arrCommentList addObject:dict];
                        [arrCommentList insertObject:dict atIndex:0];
                        [tbl_CommentList reloadData];
                    } @catch (NSException *exception) {}
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[dictJSON objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    //[alert show];
                }
            }
        } @catch (NSException *exception) {}
    }
}

- (void) imageUploadFromCamera {
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]){
        imgPkr=[[UIImagePickerController alloc]init];
        imgPkr.sourceType = UIImagePickerControllerSourceTypeCamera;
        imgPkr.delegate=(id)self;
        imgPkr.showsCameraControls=YES;
        imgPkr.allowsEditing=YES;
        imgPkr.allowsEditing=YES;
        [self presentViewController:imgPkr animated:YES completion:^{
        }];
    }
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
}

@end




