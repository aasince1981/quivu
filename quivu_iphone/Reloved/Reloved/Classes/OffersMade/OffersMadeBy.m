//
//  OffersMadeBy.m
//  Reloved
//
//  Created by Kamal on 26/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "OffersMadeBy.h"
#import "ItemDetails.h"

@interface OffersMadeBy ()

@end

@implementation OffersMadeBy

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
    
    scrollView.delegate = self;
    arrItemListing = [[NSMutableArray alloc]init];
    
    strImageShareUrl = @"";
    lblOffersMade.font = FONT_Lato_Bold(17.0f);
    lblHeaderOfferMade.font = FONT_Lato_Bold(20.0f);
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    [self GetOffersMadeData];
}

- (void) GetOffersMadeData {
    
    NSString * UserId = @"";
    if([obNet isObject:KAppDelegate.dictLoginInfo String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        @try {
            UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) { }
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@",[KAppDelegate getDictServer:WS_OfferMadeByme],UserId];
        NSLog(@"offerMadeByme Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json){
            NSLog(@"offerMadeByme Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrItemListing = [json objectForKey:@"offers"];
                    [self add_AdjustViewProductListing];
                }
            }
        }];
    }
}

#pragma mark - ShowLike Product Listing
// here we set product offer listing to view...............
- (void) add_AdjustViewProductListing {
    int xv, yv, xAxisWidth, yAxisHeight, xPoint, scrollWidth ,yBuffer, xBuffer;
    float fSize;
    
    xv = 8, yv = 36, xAxisWidth = 147, yAxisHeight = 270, xPoint = 320, scrollWidth = 320, xBuffer = 8, yBuffer = 8;
    fSize = 10.0;
    
    for(int i = 0; i< arrItemListing.count; i++){
        NSDictionary * dict = [arrItemListing objectAtIndex:i];
        
        UIView * viewimg = [[UIView alloc] init];
        viewimg.backgroundColor = [UIColor clearColor];
        viewimg.frame = CGRectMake(xv, yv, view_CategoryList.frame.size.width,view_CategoryList.frame.size.height);
        viewimg.layer.borderColor = [UIColor colorWithRed:0.733 green:0.733 blue:0.733 alpha:1].CGColor;
        viewimg.layer.borderWidth = 0.5f;
        
        UILabel * lblDivider = [[UILabel alloc]init];
        lblDivider.frame = CGRectMake(lblDividerLine.frame.origin.x, lblDividerLine.frame.origin.y, lblDividerLine.frame.size.width,lblDividerLine.frame.size.height);
        lblDivider.backgroundColor = [UIColor colorWithRed:0.733 green:0.733 blue:0.733 alpha:1];
        
        UIButton * btnrad = [[UIButton alloc] init];
        btnrad.backgroundColor = [UIColor clearColor];
        btnrad.frame = CGRectMake(btnCategoryView.frame.origin.x, btnCategoryView.frame.origin.y, btnCategoryView.frame.size.width,btnCategoryView.frame.size.height);
        [btnrad setTag:i];
        [btnrad addTarget:self action:@selector(functionToCallButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        
        // set Image Product code here
        UIImageView * imgsetImage = [[UIImageView alloc] init];
        imgsetImage.backgroundColor = [UIColor clearColor];
        imgsetImage.contentMode = UIViewContentModeScaleAspectFit;
        imgsetImage.frame = CGRectMake(imgBg.frame.origin.x, imgBg.frame.origin.y, imgBg.frame.size.width,imgBg.frame.size.height);
        
        UIImageView * imgHeaderImage = [[UIImageView alloc] init];
        imgHeaderImage.frame = CGRectMake(imgHeader.frame.origin.x, imgHeader.frame.origin.y, imgHeader.frame.size.width,imgHeader.frame.size.height);
        imgHeaderImage.image = [UIImage imageNamed:@"gray_trnsprnt_img.png"];
        
        // set Product Category code here
        UILabel * lblCategoerProductName = [[UILabel alloc]init];
        lblCategoerProductName.backgroundColor = [UIColor clearColor];
        lblCategoerProductName.frame = CGRectMake(lblProductName.frame.origin.x, lblProductName.frame.origin.y, lblProductName.frame.size.width,lblProductName.frame.size.height);
        lblCategoerProductName.textAlignment = NSTextAlignmentCenter;
        lblCategoerProductName.textColor = [UIColor blackColor];
        lblCategoerProductName.font = FONT_Lato_Bold(15.0f);
        
        UIImageView * imgBottomImage = [[UIImageView alloc] init];
        imgBottomImage.frame = CGRectMake(imgBottom.frame.origin.x, imgBottom.frame.origin.y, imgBottom.frame.size.width,imgBottom.frame.size.height);
        imgBottomImage.image = [UIImage imageNamed:@"gray_trnsprnt_img.png"];
        
        // lblProducrPrice code here
        UILabel * lblProducrPrice = [[UILabel alloc]init];
        lblProducrPrice.backgroundColor = [UIColor clearColor];
        lblProducrPrice.frame = CGRectMake(lblPrice.frame.origin.x, lblPrice.frame.origin.y, lblPrice.frame.size.width,lblPrice.frame.size.height);
        lblProducrPrice.textColor = [UIColor blackColor];
        lblProducrPrice.font = FONT_Lato_Bold(17.0f);
        
        // set userImage Code here
        UIImageView * imgUserImag = [[UIImageView alloc] init];
        imgUserImag.frame = CGRectMake(imgUserImage.frame.origin.x, imgUserImage.frame.origin.y, imgUserImage.frame.size.width,imgUserImage.frame.size.height);
        
        // lbl UserName code here
        UILabel * lblUserName = [[UILabel alloc]init];
        lblUserName.backgroundColor = [UIColor clearColor];
        lblUserName.frame = CGRectMake(lblProductUserName.frame.origin.x, lblProductUserName.frame.origin.y, lblProductUserName.frame.size.width,lblProductUserName.frame.size.height);
        lblUserName.textColor = [UIColor blackColor];
        lblUserName.font = FONT_Lato_Bold(13.0f);
        
        // set clock Image here
        UIImageView * imgClockImage = [[UIImageView alloc] init];
        imgClockImage.frame = CGRectMake(imgClock.frame.origin.x, imgClock.frame.origin.y, imgClock.frame.size.width,imgClock.frame.size.height);
        imgClockImage.image = [UIImage imageNamed:@"clock_icon.png"];
        
        
        // set ProductDuration code here
        UILabel * lblProductDuration = [[UILabel alloc]init];
        lblProductDuration.frame = CGRectMake(lblDuration.frame.origin.x, lblDuration.frame.origin.y, lblDuration.frame.size.width,lblDuration.frame.size.height);
        lblProductDuration.textColor = [UIColor blackColor];
        lblProductDuration.font = FONT_Lato_Bold(10.0f);
        
        // set Background Image on Category Like count here
        UIImageView * imgCateGoryImageLike = [[UIImageView alloc] init];
        imgCateGoryImageLike.frame = CGRectMake(imgCategoryImage.frame.origin.x, imgCategoryImage.frame.origin.y, imgCategoryImage.frame.size.width,imgCategoryImage.frame.size.height);
        imgCateGoryImageLike.image = [UIImage imageNamed:@"category_green_img.png"];
        
        // set Image LikeIcon and Like Count here
        UIImageView * imgLikeIcon = [[UIImageView alloc] init];
        imgLikeIcon.frame = CGRectMake(imgLike.frame.origin.x, imgLike.frame.origin.y, imgLike.frame.size.width,imgLike.frame.size.height);
        
        @try {
            if([obNet isObject:[dict objectForKey:@"ProductLikeInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                NSArray * arr = [dict objectForKey:@"ProductLikeInfo"];
                NSString *FromUserId = @"";
                if([obNet isObject:KAppDelegate.dictLoginInfo String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
                    FromUserId = [NSString stringWithFormat:@"%d",[[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] intValue]];
                }
                
                if(arr.count > 0) {
                    for (int i=0; i < arr.count; i++) {
                        NSString * str = [NSString stringWithFormat:@"%d",[[[arr objectAtIndex:i] objectForKey:@"UserId"] intValue]];
                        if(str.intValue == FromUserId.intValue) {
                            imgLikeIcon.image = [UIImage imageNamed:@"like_red_img.png"];
                            break;
                        } else {
                            imgLikeIcon.image = [UIImage imageNamed:@"like_white_icon.png"];
                        }
                    }
                } else {
                    imgLikeIcon.image = [UIImage imageNamed:@"like_white_icon.png"];
                }
            }
            
        } @catch (NSException *exception) { }
        
        
        UIButton * btnLike = [[UIButton alloc]init];
        btnLike.frame = CGRectMake(BtnLike.frame.origin.x, BtnLike.frame.origin.y, BtnLike.frame.size.width,BtnLike.frame.size.height);
        [btnLike addTarget:self action:@selector(BtnLikeAction:) forControlEvents:UIControlEventTouchUpInside];
        [btnLike setTag:i];
        
        UILabel * lblCountLike = [[UILabel alloc]init];
        lblCountLike.backgroundColor = [UIColor clearColor];
        lblCountLike.frame = CGRectMake(lblLikeCount.frame.origin.x, lblLikeCount.frame.origin.y, lblLikeCount.frame.size.width,lblLikeCount.frame.size.height);
        lblCountLike.textColor = [UIColor whiteColor];
        lblCountLike.textAlignment = NSTextAlignmentCenter;
        lblCountLike.font = FONT_Lato_Bold(15.0f);
        
        // set Divider image here
        UIImageView * imgDividerImage = [[UIImageView alloc] init];
        imgDividerImage.frame = CGRectMake(imgDivider.frame.origin.x, imgDivider.frame.origin.y, imgDivider.frame.size.width,imgDivider.frame.size.height);
        imgDividerImage.backgroundColor = [UIColor whiteColor];
        //imgDividerImage.image = [UIImage imageNamed:@"green_divider_category.png"];
        
        // set ShareIcon image here
        UIImageView * imgShareIcon = [[UIImageView alloc] init];
        imgShareIcon.frame = CGRectMake(imgShare.frame.origin.x, imgShare.frame.origin.y, imgShare.frame.size.width,imgShare.frame.size.height);
        imgShareIcon.image = [UIImage imageNamed:@"share_icon_white.png"];
        
        UIButton * btnShareIcon = [[UIButton alloc]init];
        btnShareIcon.frame = CGRectMake(BtnShare.frame.origin.x, BtnShare.frame.origin.y, BtnShare.frame.size.width,BtnShare.frame.size.height);
        [btnShareIcon addTarget:self action:@selector(BtnShareAction:) forControlEvents:UIControlEventTouchUpInside];
        [btnShareIcon setTag:i];
        
        if([obNet isObject:[dict objectForKey:@"OfferProductName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            lblCategoerProductName.text = [dict objectForKey:@"OfferProductName"];
        }
        
        @try {
            lblProducrPrice.text = [NSString stringWithFormat:@"$ %d",[[dict objectForKey:@"OfferAmount"] intValue]];
        } @catch (NSException *exception) { }
        
        @try {
            lblCountLike.text = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"ProductLikeCount"] intValue]];
        } @catch (NSException *exception) { }
        
        if([obNet isObject:[dict objectForKey:@"ProductUserName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            lblUserName.text = [dict objectForKey:@"ProductUserName"];
        }
        
        @try {
            lblProductDuration.text = [NSString stringWithFormat:@"%@",[KAppDelegate getTimeDiffrece:[dict objectForKey:@"OfferAddTime"]]];
        } @catch (NSException *exception) { }
        
        if([obNet isObject:[dict objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            @try {
                lblCategoerProductName.text = [dict objectForKey:@"OfferProductName"];
            } @catch (NSException *exception) { }
        }
        
        NSArray * arr = [[NSArray alloc]init];
        if([obNet isObject:[dict objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            arr = [dict objectForKey:@"ProductImageInfo"];
        }
        
        @try {
            if(arr.count > 0) {
                @try {
                    NSDictionary * dict = [arr objectAtIndex:0];
                    strImageShareUrl =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[dict objectForKey:@"ProImageName"]];
                    
                    //[obNet SetImageToView:imgsetImage fromImageUrl:strImageShareUrl Option:5];
                    [imgsetImage GetNSetUIImage:strImageShareUrl DefaultImage:@"no_image.png"];
                } @catch (NSException *exception) {}
            }
        } @catch (NSException *exception) {}
        
        @try {
            NSString * strImageUrl =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"ProductUserImage"]];
            //[obNet SetImageToView:imgUserImag fromImageUrl:strImageUrl Option:5];
            [imgUserImag GetNSetUIImage:strImageUrl DefaultImage:@"default_user.png"];
        } @catch (NSException *exception) {}
        
        
        
        CGRect rectai = CGRectMake(imgsetImage.frame.origin.x-20+(imgsetImage.frame.size.width/2), imgsetImage.frame.origin.y-20+(imgsetImage.frame.size.height/2), 40, 40);
        UIActivityIndicatorView * ai = [[UIActivityIndicatorView alloc] initWithFrame:rectai];
        [ai setHidden:NO];
        
        [ai setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhiteLarge];
        [ai startAnimating];
        [ai setBackgroundColor:[UIColor clearColor]];
        [ai setColor:[UIColor blackColor]];
        
        [viewimg addSubview:ai];
        [viewimg addSubview:imgsetImage];
        [viewimg addSubview:imgHeaderImage];
        [viewimg addSubview:lblCategoerProductName];
        [viewimg addSubview:imgBottomImage];
        [viewimg addSubview:lblProducrPrice];
        [viewimg addSubview:imgUserImag];
        [viewimg addSubview:lblUserName];
        [viewimg addSubview:imgClockImage];
        [viewimg addSubview:lblProductDuration];
        [viewimg addSubview:lblDivider];
        [viewimg addSubview:imgCateGoryImageLike];
        [viewimg addSubview:imgLikeIcon];
        [viewimg addSubview:lblCountLike];
        [viewimg addSubview:imgDividerImage];
        [viewimg addSubview:imgShareIcon];
        [viewimg addSubview:btnrad];
        [viewimg addSubview:btnLike];
        [viewimg addSubview:btnShareIcon];
        [viewimg setTag:i];
        
        xv = xv + xAxisWidth+ xBuffer;
        
        //To change into down side & again start from previous position of x
        if(xv > xPoint) {
            yv = yv + yAxisHeight + yBuffer;
            
            xv = 8;
            
            viewimg.frame = CGRectMake(xv, yv, view_CategoryList.frame.size.width,view_CategoryList.frame.size.height);
            xv = xv + xAxisWidth + xBuffer;
        }
        [scrollView addSubview:viewimg];
    }
    [scrollView setContentSize:CGSizeMake(scrollWidth, yv+yAxisHeight+40)];
}

- (void) functionToCallButtonAction: (id) sender {
    
    @try {
        int tag = [sender tag];
        static NSInteger dir = 0;
        ItemDetails * item = [[ItemDetails alloc]initWithNibName:@"ItemDetails" bundle:nil];
        dir++;
        
        if([obNet isObject:arrItemListing String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            NSDictionary * dict = [arrItemListing objectAtIndex:tag];
            NSLog(@"dict--> %@",dict);
            
            NSString * ProductId = [dict objectForKey:@"ProductId"];
            NSString * ProductCatId = [dict objectForKey:@"ProductCatId"];
            
            if(ProductId != nil)
                item.ProductId = [NSString stringWithFormat:@"%@",ProductId];
            
            if(ProductCatId != nil)
                item.ProductCatId = [NSString stringWithFormat:@"%@",ProductCatId];
        }
        
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        item.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:item animated:YES];
    } @catch (NSException *exception) { }
}

#pragma mark - Button Actions
// Here we call Like Unlike webservice.................
- (void) BtnLikeAction: (id) sender {
    UIButton *buttonThatWasPressed = (UIButton *)sender;
    buttonThatWasPressed.enabled = NO;
    
    NSString *FromUserId, *FromUserName, *ToUserId, *ToUserName, *LikeProductId, *LikeProductName, *LikeStatus, *FromUserImage, *ProductImage;
    FromUserId = @"";
    FromUserName = @"";
    ToUserId = @"";
    ToUserName = @"";
    LikeProductId = @"";
    LikeProductName = @"";
    LikeStatus = @"";
    FromUserImage = @"";
    ProductImage = @"";
    
    int tag = [sender tag];
    
    NSDictionary * dict = [[NSDictionary alloc]init];
    
    if([obNet isObject:arrItemListing String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
        dict = [arrItemListing objectAtIndex:tag];
    }
    
    if(dict.count > 0) {
        
        @try {
            LikeProductId =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"ProductId"] intValue]];
        } @catch (NSException *exception) { }
        
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
        
        @try {
            if([obNet isObject:[dict objectForKey:@"ProductLikeInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                NSArray * arr = [dict objectForKey:@"ProductLikeInfo"];
                
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
        
        @try {
            LikeProductName = [dict objectForKey:@"ProductName"];
        } @catch (NSException *exception) { }
        
        @try {
            
            if([obNet isObject:[dict objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                NSArray * arr = [dict objectForKey:@"ProductImageInfo"];
                NSDictionary * ddd = [arr objectAtIndex:0];
                if(arr.count > 0) {
                    ProductImage = [ddd objectForKey:@"ProImageName"];
                }
            }
            
        } @catch (NSException *exception) { }
        
        @try {
            ToUserName = [dict objectForKey:@"ProductUserName"];
        } @catch (NSException *exception) { }
        
        @try {
            ToUserId = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"ProductUserId"] intValue]];
        } @catch (NSException *exception) { }
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FromUserId=%@&FromUserName=%@&ToUserId=%@&ToUserName=%@&LikeProductId=%@&LikeProductName=%@&LikeStatus=%@&FromUserImage=%@&ProductImage=%@",[KAppDelegate getDictServer:WS_AddLike],FromUserId,FromUserName,ToUserId,ToUserName,LikeProductId,LikeProductName,LikeStatus,FromUserImage,ProductImage];
        NSLog(@"AddLike Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"AddLike Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [self GetOffersMadeData];
                    buttonThatWasPressed.enabled = YES;
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

// here we share image to socials.............
- (void) BtnShareAction: (id) sender {
    int tag = [sender tag];
    NSMutableDictionary * dictProductDetails = [[NSMutableDictionary alloc]init];
    if(arrItemListing.count > 0) {
        dictProductDetails = [arrItemListing objectAtIndex:tag];
    }
    
    NSArray * arr = [[NSArray alloc]init];
    if([obNet isObject:[dictProductDetails objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
        arr = [dictProductDetails objectForKey:@"ProductImageInfo"];
    }
    
    NSString * strurl;
    
    @try {
        if(arr.count > 0) {
            @try {
                NSDictionary * dict = [arr objectAtIndex:0];
                strurl =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[dict objectForKey:@"ProImageName"]];
            } @catch (NSException *exception) {}
        }
    } @catch (NSException *exception) {}
    
    NSURL *shareUrl = [NSURL URLWithString:strurl];
    
    NSArray *activityItems = [NSArray arrayWithObjects:shareUrl, nil];
    
    UIActivityViewController *activityViewController = [[UIActivityViewController alloc] initWithActivityItems:activityItems applicationActivities:nil];
    activityViewController.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
    
    [self presentViewController:activityViewController animated:YES completion:nil];
}

#pragma mark - Button Actions

- (IBAction)Btnback:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)BtnReferesh:(id)sender {
    [self GetOffersMadeData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
