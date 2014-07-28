//
//  ItemDetails.m
//  Reloved
//
//  Created by Kamal on 21/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ItemDetails.h"
#import "ProfileOther.h"
#import "ViewChatViewController.h"
#import "CommentScreen.h"
#import "CommentListCell.h"
#import "ViewOfferScreen.h"
#import "ItemLikesScreen.h"
#import "AddItemScreen.h"
#import "EditItemScreen.h"

@interface ItemDetails ()
{
    NSDictionary * dictProductDetails;
    NSString *FromUserId;
    NSString *FromUserName;
    NSString *FromUserImage;
    NSArray * arrProductImageInfo;
}

@end

@implementation ItemDetails
@synthesize ProductId,CategoryName,ProductCatId,flageBtnBackNavigation;

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
    dictProductDetails = [[NSDictionary alloc]init];
    arrCommentList = [[NSMutableArray alloc]init];
    arrProductImageInfo = [[NSArray alloc]init];
    
    scrollView.delegate = self;
    scrollViewFlipper.delegate = self;
    sv_ScrollviewBigImage.delegate = self;
    
    tbl_CommentList.dataSource = self;
    tbl_CommentList.delegate = self;
    
    [self SetFontType];
    tf_OfferPrice.delegate = self;
    
    strImageShareUrl = @"";
    FromUserId = @"";
    FromUserName = @"";
    FromUserImage = @"";
}

// set font type and size.....
- (void) SetFontType {
    lblItemDetails.font = FONT_Lato_Bold(20.0f);
    lblUserName.font = FONT_Lato_Bold(17.0f);
    lblDuration.font = FONT_Lato_Light(12.0f);
    lblProductName.font = FONT_Lato_Bold(17.0f);
    lblLikeCount.font = FONT_Lato_Bold(17.0f);
    lblPrice.font = FONT_Lato_Bold(17.0f);
    lblPriceOffered.font = FONT_Lato_Bold(17.0f);
    btnChatToBuy.titleLabel.font = FONT_Lato_Bold(17.0f);
    lblYourOffer.font = FONT_Lato_Bold(20.0f);
    BtnCancel.titleLabel.font = FONT_Lato_Bold(17.0f);
    BtnDone.titleLabel.font = FONT_Lato_Bold(17.0f);
    lblTapToChange.font = FONT_Lato_Bold(14.0f);
    tf_OfferPrice.font = FONT_Lato_Bold(22.0f);
    lblUserNameSellingOffer.font = FONT_Lato_Bold(15.0f);
    lblMakeAnOffer.font = FONT_Lato_Bold(16.0f);
    lblDollerIcon.font = FONT_Lato_Bold(22.0f);
}

#pragma mark - viewWillAppear Methods
- (void) viewWillAppear:(BOOL)animated
{
    KAppDelegate.dictDeleteProductId = [NSMutableDictionary new];
    KAppDelegate.MyVC = self;
    
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
    
    [self DownloadItemDetailsData];
}

// here we get viewProduct data...............
- (void) DownloadItemDetailsData {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&ProductId=%@",[KAppDelegate getDictServer:WS_ViewProduct],ProductId];
        NSLog(@"ViewProduct Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            
            NSLog(@"ViewProduct Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    
                    dictProductDetails = [json objectForKey:@"ProductInformation"];
                    
                    if([obNet isObject:[dictProductDetails objectForKey:@"ProductCommentInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                        @try {
                            arrCommentList = [dictProductDetails objectForKey:@"ProductCommentInfo"];
                        } @catch (NSException *exception) {}
                    }
                    
                    NSString * ProductUserId = [dictProductDetails objectForKey:@"ProductUserId"];
                    NSString * SoldStatus = [dictProductDetails objectForKey:@"ProductSoldStatus"];
                    if(FromUserId.intValue == ProductUserId.intValue) {
                        
                        if(SoldStatus.intValue == 1) {
                            btnEdit.hidden = YES;
                            btnReferesh.frame = CGRectMake(289,btnReferesh.frame.origin.y, btnReferesh.frame.size.width, btnReferesh.frame.size.height);
                        } else {
                            btnEdit.hidden = NO;
                            btnReferesh.frame = CGRectMake(246,btnReferesh.frame.origin.y, btnReferesh.frame.size.width, btnReferesh.frame.size.height);
                        }
                        btnUserTwo.enabled = NO;
                        [self.view addSubview:btnReferesh];
                    } else {
                        btnUserTwo.enabled = YES;
                        btnEdit.hidden = YES;
                        btnReferesh.frame = CGRectMake(289,btnReferesh.frame.origin.y, btnReferesh.frame.size.width, btnReferesh.frame.size.height);
                        [self.view addSubview:btnReferesh];
                    }
                    
                    [tbl_CommentList reloadData];
                    [self SetProductDetailsData];
                    
                    // [scrollView setContentSize:CGSizeMake(320, arrCommentList.count * 320)];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

// here we set product details..........
- (void) SetProductDetailsData {
    
    if(dictProductDetails.count > 0) {
        arrProductImageInfo = [dictProductDetails objectForKey:@"ProductImageInfo"];
        int x = 90;
        if(arrProductImageInfo.count > 0) {
            for (int i =0; i < arrProductImageInfo.count; i++) {
                CGRect frame;
                frame.origin.x = scrollViewFlipper.frame.size.width * i;
                frame.origin.y = 0;
                frame.size = scrollViewFlipper.frame.size;
                
                UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(x, 0, 140, 142)];
                imageView.contentMode = UIViewContentModeScaleToFill;
                
                strImageShareUrl =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arrProductImageInfo objectAtIndex:i] objectForKey:@"ProImageName"]];
                //[obNet SetImageToView:imageView fromImageUrl:strImageShareUrl Option:5];
                [imageView GetNSetUIImage:strImageShareUrl DefaultImage:@"no_image.png"];
                
                UIButton * btn = [[UIButton alloc]initWithFrame:CGRectMake(x, 0, 140, 142)];
                [btn addTarget:self action:@selector(ActionOnImageClick:) forControlEvents:UIControlEventTouchUpInside];
                [btn setTag:i];
                [scrollViewFlipper addSubview:imageView];
                [scrollViewFlipper addSubview:btn];
                
                if(arrProductImageInfo.count > 1)
                    scrollViewFlipper.contentSize=CGSizeMake(scrollViewFlipper.frame.size.width * i + 250,scrollViewFlipper.frame.size.height);
                x = x + 250;
            }
        }
        
        @try {
            lblDuration.text = [NSString stringWithFormat:@"%@",[KAppDelegate getTimeDiffrece:[dictProductDetails objectForKey:@"ProductAddDate"]]];
        } @catch (NSException *exception) {}
        
        @try {
            lblUserName.text = [dictProductDetails objectForKey:@"ProductUserName"];
        } @catch (NSException *exception) {}
        
        @try {
            NSString * strImageUrl =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dictProductDetails objectForKey:@"ProductUserImage"]];
            [obNet SetImageToView:imgProductUserImage fromImageUrl:strImageUrl Option:5];
        } @catch (NSException *exception) {}
        
        @try {
            lblProductName.text = [dictProductDetails objectForKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            lblPrice.text = [NSString stringWithFormat:@"$ %@",[dictProductDetails objectForKey:@"ProductPrice"]];
        } @catch (NSException *exception) {}
        
        @try {
            lblLikeCount.text = [NSString stringWithFormat:@"%d",[[dictProductDetails objectForKey:@"ProductLikeCount"] intValue]];
        } @catch (NSException *exception) {}
        
        // Check Product is Already Liked or not
        @try {
            if([obNet isObject:[dictProductDetails objectForKey:@"ProductLikeInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                NSArray * arr = [dictProductDetails objectForKey:@"ProductLikeInfo"];
                
                if(arr.count > 0) {
                    for (int i=0; i < arr.count; i++) {
                        NSString * str = [NSString stringWithFormat:@"%d",[[[arr objectAtIndex:i] objectForKey:@"UserId"] intValue]];
                        if(str.intValue == FromUserId.intValue) {
                            [BtnLike setBackgroundImage:[UIImage imageNamed:@"like_circle_red.png"] forState:UIControlStateNormal];
                            imgLikeBordered.image = [UIImage imageNamed:@"like_red_img.png"];
                            break;
                        } else {
                            [BtnLike setBackgroundImage:[UIImage imageNamed:@"like_circle.png"] forState:UIControlStateNormal];
                            imgLikeBordered.image = [UIImage imageNamed:@"like_boder_img.png"];
                        }
                    }
                } else {
                    [BtnLike setBackgroundImage:[UIImage imageNamed:@"like_circle.png"] forState:UIControlStateNormal];
                    imgLikeBordered.image = [UIImage imageNamed:@"like_boder_img.png"];
                }
            }
        } @catch (NSException *exception) { }
        
        // Check Product is Already Offered or not
        @try {
            if(dictProductDetails.count > 0){
                NSArray * arrOffer = [[NSArray alloc]init];
                if([obNet isObject:[dictProductDetails objectForKey:@"ProductOfferInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                    arrOffer = [dictProductDetails objectForKey:@"ProductOfferInfo"];
                }
                
                NSString * ProductUserId = [dictProductDetails objectForKey:@"ProductUserId"];
                NSString * ProductOfferCount = [dictProductDetails objectForKey:@"ProductOfferCount"];
                NSString * ProductSoldStatus = [dictProductDetails objectForKey:@"ProductSoldStatus"];
                
                if (ProductSoldStatus.intValue == 0) {
                    
                    if (FromUserId.intValue == ProductUserId.intValue) {
                        if (ProductOfferCount.intValue == 0 || [ProductOfferCount isEqualToString:@""] ) {
                            lblPriceOffered.text = [NSString stringWithFormat:@"No Offers yet"];
                            btnChatToBuy.hidden = YES;
                        } else {
                            btnChatToBuy.hidden = NO;
                            lblPriceOffered.text = [NSString stringWithFormat:@"You have %d offer",ProductOfferCount.intValue];
                            [btnChatToBuy setTitle:@"View Offer" forState:UIControlStateNormal];
                        }
                    } else {
                        NSString * userId = @"";
                        if(arrOffer.count > 0) {
                            for (int i = 0; i < arrOffer.count; i++) {
                                userId = [[arrOffer objectAtIndex:i] objectForKey:@"UserId"];
                                if(FromUserId.intValue == userId.intValue) {
                                    NSString * str = [NSString stringWithFormat:@"%@",[[arrOffer objectAtIndex:i] objectForKey:@"Amount"]];
                                    float val = [str floatValue];
                                    lblPriceOffered.text = [NSString stringWithFormat:@"You Offered $ %.2f",val];
                                    [btnChatToBuy setTitle:@"View chat" forState:UIControlStateNormal];
                                    break;
                                } else {
                                    NSString * str = [NSString stringWithFormat:@"%@",[dictProductDetails objectForKey:@"ProductPrice"]];
                                    float val = [str floatValue];
                                    lblPriceOffered.text = [NSString stringWithFormat:@"$ %.2f",val];
                                    [btnChatToBuy setTitle:@"Chat to buy" forState:UIControlStateNormal];
                                }
                            }
                        } else {
                            NSString * str = [NSString stringWithFormat:@"%@",[dictProductDetails objectForKey:@"ProductPrice"]];
                            float val = [str floatValue];
                            lblPriceOffered.text = [NSString stringWithFormat:@"$ %.2f",val];
                            [btnChatToBuy setTitle:@"Chat to buy" forState:UIControlStateNormal];
                        }
                    }
                } else {
                    
                    if (FromUserId.intValue == ProductUserId.intValue) {
                        lblPriceOffered.text = [NSString stringWithFormat:@"You sold this item"];
                        btnChatToBuy.hidden = YES;
                    } else {
                        btnChatToBuy.hidden = YES;
                        lblPriceOffered.text = @"SOLD";
                    }
                }
            }
        } @catch (NSException *exception) { }
    }
}

// here we add flipper on image click..........
- (void) ActionOnImageClick: (id) sender {
   // NSInteger tag = [sender tag];
    
    View_BigImage.frame = self.view.frame;
    [self.view addSubview:View_BigImage];
    
    int x = 0;
    int xBuffer = 289;
    
    if(arrProductImageInfo.count > 0) {
        for (int i =0; i < arrProductImageInfo.count; i++) {
            CGRect frame;
            frame.origin.x = sv_ScrollviewBigImage.frame.size.width * i;
            frame.origin.y = 0;
            frame.size = sv_ScrollviewBigImage.frame.size;
            sv_ScrollviewBigImage.backgroundColor = [UIColor clearColor];
            
            UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(x, 0, 289, 269)];
            imageView.contentMode = UIViewContentModeScaleToFill;
            
            strImageShareUrl =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arrProductImageInfo objectAtIndex:i] objectForKey:@"ProImageName"]];
            [imageView GetNSetUIImage:strImageShareUrl DefaultImage:@"no_image.png"];
            
            [sv_ScrollviewBigImage addSubview:imageView];
            
            if(arrProductImageInfo.count > 1)
                sv_ScrollviewBigImage.contentSize=CGSizeMake(sv_ScrollviewBigImage.frame.size.width * i + xBuffer,sv_ScrollviewBigImage.frame.size.height);
            
            x = x + 320;
            xBuffer = xBuffer + 60;
        }
    }
}

#pragma mark - Button Actions
- (IBAction)BtnChatToBuy:(id)sender {
    NSString * buttonTitle = btnChatToBuy.titleLabel.text;
    
    if([buttonTitle isEqualToString:@"Chat to buy"]) {
        NSString * price;
        NSString * ProductName;
        
        @try {
            if([dictProductDetails objectForKey:@"ProductPrice"] != nil) {
                price = [NSString stringWithFormat:@"%@",[dictProductDetails objectForKey:@"ProductPrice"]];
            }
            
            if([dictProductDetails objectForKey:@"ProductUserName"] != nil) {
                ProductName = [dictProductDetails objectForKey:@"ProductUserName"];
            }
            
            lblUserNameSellingOffer.text = [NSString stringWithFormat:@"%@ is selling it for $%@",ProductName,price];
            
            NSString * str = [NSString stringWithFormat:@"%@",price];
            float val = [str floatValue];
            tf_OfferPrice.text = [NSString stringWithFormat:@"%.2f",val];
            
        } @catch (NSException *exception) {}
        viewOffered.frame = self.view.frame;
        [self.view addSubview:viewOffered];
        
    } else if ([buttonTitle isEqualToString:@"View chat"]) {
        static NSInteger dir = 0;
        ViewChatViewController * view = [[ViewChatViewController alloc]initWithNibName:@"ViewChatViewController" bundle:nil];
        dir++;
        
        if([obNet isObject:dictProductDetails String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
            NSString * str = [dictProductDetails objectForKey:@"ProductUserId"];
            if(str != nil) {
                view.ProductId = [NSString stringWithFormat:@"%@",ProductId];
            }
        }
        
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        view.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:view animated:YES];
        
    } else if ([buttonTitle isEqualToString:@"View Offer"]) {
        static NSInteger dir = 0;
        ViewOfferScreen * vc = [[ViewOfferScreen alloc]initWithNibName:@"ViewOfferScreen" bundle:nil];
        dir++;
        
        if([obNet isObject:dictProductDetails String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
            if(dictProductDetails.count > 0) {
                NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
                dict = [dictProductDetails mutableCopy];
                
                NSString * str = [NSString stringWithFormat:@"%@",ProductId];
                
                if(str != nil) {
                    [dict setObject:ProductId forKey:@"ProductId"];
                }
                vc.DictProductDetails = dict;
            }
        }
        
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
    }
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

// add offer button on done button click...........
- (IBAction)BtnDone:(id)sender {
    NSString  * ToUserId = @"";
    NSString  * ProductName = @"";
    NSString  * ProductImage = @"";
    
    if(dictProductDetails.count > 0){
        @try {
            ToUserId = [dictProductDetails objectForKey:@"ProductUserId"];
        } @catch (NSException *exception) { }
        
        @try {
            ProductName = [dictProductDetails objectForKey:@"ProductUserName"];
        } @catch (NSException *exception) { }
        
        @try {
            ProductImage = [dictProductDetails objectForKey:@"ProductUserImage"];
        } @catch (NSException *exception) { }
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&OfferFromUserId=%@&OfferFromUserName=%@&OfferFromUserImage=%@&OfferToUserId=%@&OfferProductId=%@&OfferProductName=%@&OfferProductImage=%@&OfferAmount=%@",[KAppDelegate getDictServer:WS_AddOffer],FromUserId,FromUserName,FromUserImage,ToUserId,ProductId,ProductName,ProductImage,tf_OfferPrice.text];
        NSLog(@"AddOffer Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"AddOffer Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [viewOffered removeFromSuperview];
                    [self DownloadItemDetailsData];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

- (IBAction)BtnCancel:(id)sender {
    [viewOffered removeFromSuperview];
}

- (IBAction)BtnReferesh:(id)sender {
    [self DownloadItemDetailsData];
}

// here we go to other user profile..........
- (IBAction)BtnUserTwo:(id)sender {
    static NSInteger dir = 0;
    ProfileOther * profile = [[ProfileOther alloc]initWithNibName:@"ProfileOther" bundle:nil];
    dir++;
    
    if([obNet isObject:dictProductDetails String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        NSString * str = [dictProductDetails objectForKey:@"ProductUserId"];
        if(str != nil) {
            profile.ProductUserId = [NSString stringWithFormat:@"%@",str];
        }
    }
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    profile.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:profile animated:YES];
}

//ItemLikesUser product image........
- (IBAction)BtnItemLikesUser:(id)sender {
    static NSInteger dir = 0;
    ItemLikesScreen * vcItemLike = [[ItemLikesScreen alloc]initWithNibName:@"ItemLikesScreen" bundle:nil];
    dir++;
    
    if([obNet isObject:dictProductDetails String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        NSString * str = [dictProductDetails objectForKey:@"ProductUserId"];
        if(str != nil) {
            vcItemLike.ProductId = [NSString stringWithFormat:@"%@",ProductId];
        }
    }
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    vcItemLike.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vcItemLike animated:YES];
}

- (IBAction)btnCross:(id)sender {
    [View_BigImage removeFromSuperview];
}

// button to go edit item screen............
- (IBAction)BtnEdit:(id)sender {
    static NSInteger dir = 0;
    EditItemScreen * add = [[EditItemScreen alloc]initWithNibName:@"EditItemScreen" bundle:nil];
    dir++;
    KAppDelegate.boolForEditProduct = YES;
    
    @try {
        NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
        dict = [dictProductDetails mutableCopy];
        if(CategoryName != nil)
            [dict setObject:CategoryName forKey:@"CategoryName"];
        
        if(ProductCatId != nil)
            [dict setObject:ProductCatId forKey:@"ProductCatId"];
        
        if(ProductId != nil)
            [dict setObject:ProductId forKey:@"ProductId"];
        
        //add.dictProductInfo = dict;
        KAppDelegate.DictEditProductInfo = dict;
        KAppDelegate.dictDeleteProductId = [NSMutableDictionary new];
        KAppDelegate.dictImages = [[NSMutableDictionary alloc]init];
        KAppDelegate.dictCategoryInfo = [[NSMutableDictionary alloc]init];
        KAppDelegate.imgCoverPhoto = nil;
    } @catch (NSException *exception) {}
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    add.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:add animated:YES];
}

//like and unlike product image........
- (IBAction)BtnLike:(id)sender {
    NSString  *ToUserId = @"";
    NSString  *ToUserName = @"";
    NSString  *LikeProductName = @"";
    NSString  *LikeStatus = @"";
    NSString  *ProductImage = @"";
    
    if(dictProductDetails.count > 0) {
        @try {
            if([obNet isObject:[dictProductDetails objectForKey:@"ProductLikeInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                NSArray * arr = [dictProductDetails objectForKey:@"ProductLikeInfo"];
                
                if(arr.count > 0) {
                    for (int i=0; i < arr.count; i++) {
                        NSString * str = [NSString stringWithFormat:@"%d",[[[arr objectAtIndex:i] objectForKey:@"UserId"] intValue]];
                        
                        if(str.intValue == FromUserId.intValue) {
                            LikeStatus = @"0";;
                            break;
                        } else {
                            LikeStatus = @"1";
                        }
                    }
                } else {
                    LikeStatus = @"1";
                }
            }
        } @catch (NSException *exception) { }
        
        if([obNet isObject:[dictProductDetails objectForKey:@"ProductName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            LikeProductName = [dictProductDetails objectForKey:@"ProductName"];
        }
        
        @try {
            if([obNet isObject:[dictProductDetails objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                NSArray * arr = [dictProductDetails objectForKey:@"ProductImageInfo"];
                NSDictionary * ddd = [arr objectAtIndex:0];
                if(arr.count > 0) {
                    ProductImage = [ddd objectForKey:@"ProImageName"];
                }
            }
        } @catch (NSException *exception) { }
        
        if([obNet isObject:[dictProductDetails objectForKey:@"ProductUserName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            ToUserName = [dictProductDetails objectForKey:@"ProductUserName"];
        }
        
        @try {
            ToUserId = [NSString stringWithFormat:@"%d",[[dictProductDetails objectForKey:@"ProductUserId"] intValue]];
        } @catch (NSException *exception) { }
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FromUserId=%@&FromUserName=%@&ToUserId=%@&ToUserName=%@&LikeProductId=%@&LikeProductName=%@&LikeStatus=%@&FromUserImage=%@&ProductImage=%@",[KAppDelegate getDictServer:WS_AddLike],FromUserId,FromUserName,ToUserId,ToUserName,ProductId,LikeProductName,LikeStatus,FromUserImage,ProductImage];
        NSLog(@"AddLike Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"AddLike Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [self DownloadItemDetailsData];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

// here we share image to social..........
- (IBAction)BtnShare:(id)sender {
    NSURL *shareUrl = [NSURL URLWithString:strImageShareUrl];
    NSArray *activityItems = [NSArray arrayWithObjects:shareUrl, nil];
    UIActivityViewController *activityViewController = [[UIActivityViewController alloc] initWithActivityItems:activityItems applicationActivities:nil];
    activityViewController.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
    [self presentViewController:activityViewController animated:YES completion:nil];
}

 // here to go from comment screen...........
- (IBAction)BtnComment:(id)sender {
    static NSInteger dir = 0;
    CommentScreen * vc = [[CommentScreen alloc]initWithNibName:@"CommentScreen" bundle:nil];
    dir++;
    vc.ProductId = ProductId;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - textField Delegate Methods
- (BOOL) textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}

-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    if( textField == tf_OfferPrice) {
        double currentValue = [tf_OfferPrice.text doubleValue];
        double cents = round(currentValue * 100.0f);
        
        if ([string length]) {
            for (size_t i = 0; i < [string length]; i++) {
                unichar c = [string characterAtIndex:i];
                if (isnumber(c)) {
                    cents *= 10;
                    cents += c - '0';
                }
            }
        } else {
            cents = floor(cents / 10);
        }
        tf_OfferPrice.text = [NSString stringWithFormat:@"%.2f", cents / 100.0f];
        
        return NO;
    }
    
    return YES;
}

#pragma mark - scrollViewDidScroll Delegate Methods
- (void)scrollViewDidScroll:(UIScrollView *)sender {
    if (!pageControl) {
        CGFloat pageWidth = scrollViewFlipper.frame.size.width;
        int page = floor((scrollViewFlipper.contentOffset.x - pageWidth / 2) / pageWidth) + 1;
        pageControl.currentPage = page;
    }
}

#pragma mark - TabliView DataSource and Delegates methods
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return arrCommentList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *strCell = @"cell";
    CommentListCell *cell = (CommentListCell *)[tableView dequeueReusableCellWithIdentifier:strCell];
    
    if(cell == nil) {
        cell = [[[NSBundle mainBundle]loadNibNamed:@"CommentListCell" owner:self options:nil]objectAtIndex:0];
    }
    
    NSDictionary * dict = [arrCommentList objectAtIndex:indexPath.row];
    
    cell.lblUserName.text = [NSString stringWithFormat:@"%@",[dict objectForKey:@"CommentUserName"]];
    cell.lblCommentMessage.text = [NSString stringWithFormat:@"%@",[dict objectForKey:@"CommentMessage"]];
    cell.lblCommentMessage.lineBreakMode = NSLineBreakByWordWrapping;
    cell.lblCommentMessage.numberOfLines = 0;
    [cell.lblCommentMessage sizeToFit];
    
    cell.lblDuration.text = [KAppDelegate getTimeDiffrece:[dict objectForKey:@"CommentTime"]];
    
    @try {
        NSString * str =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"CommentUserImage"]];
        [obNet SetImageToView:cell.imgUserImage fromImageUrl:str Option:5];
    } @catch (NSException *exception) {}
    
    cell.selectionStyle=UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary * dict = [arrCommentList objectAtIndex:indexPath.row];
    NSString * strId;
    NSString * LoginId;
    
    @try {
        strId = [dict objectForKey:@"CommentUserId"];
    } @catch (NSException *exception) {}
    
    @try {
        LoginId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
    } @catch (NSException *exception) {}
    
    if(LoginId.intValue != strId.intValue) {
        static NSInteger dir = 0;
        ProfileOther * profile = [[ProfileOther alloc]initWithNibName:@"ProfileOther" bundle:nil];
        dir++;
        
        if(strId != nil) {
            profile.ProductUserId = [NSString stringWithFormat:@"%@",strId];
        }
        
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        profile.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:profile animated:YES];
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSDictionary * dict = [arrCommentList objectAtIndex:indexPath.row];
    CGSize size =   [self sizeOfText:[dict objectForKey:@"CommentMessage"] widthOfLabel:164 withFont:FONT_Lato_Bold(12.0f)];
    UILabel * lbl = [[UILabel alloc]init];
    lbl.text = [dict objectForKey:@"CommentMessage"];
    
    return 45.0+size.height;
}

-(CGSize)sizeOfText:(NSString *)textToMesure widthOfLabel:(CGFloat)width withFont:(UIFont*)font
{
    CGSize ts = [textToMesure sizeWithFont:font constrainedToSize:CGSizeMake(width-20.0, FLT_MAX) lineBreakMode:NSLineBreakByWordWrapping];
    return ts;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
