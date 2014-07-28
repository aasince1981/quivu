//
//  ProfileOther.m
//  Reloved
//
//  Created by Kamal on 22/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ProfileOther.h"
#import "ItemDetails.h"
#import  "Following.h"
#import "Followers.h"
#import "FeedBackScreen.h"

@interface ProfileOther ()
{
    NSString * websiteUrl;
}
@end

@implementation ProfileOther
{
    NSMutableDictionary * dictProfileData;
}
@synthesize ProductUserId;

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
    
    arrItemListing = [[NSMutableArray alloc]init];
    scrollView.delegate = self;
    [self SetFontType];
    strImageShareUrl = @"";
    dictProfileData = [[NSMutableDictionary alloc]init];
}

#pragma mark - viewWillAppear Method
- (void) viewWillAppear:(BOOL)animated {
    KAppDelegate.MyVC = self;
    [self DownLoadProfileData];
}

// here we set font type and size.......................
- (void) SetFontType {
    lblHeader.font = FONT_Lato_Bold(20.0f);
    lblUserName.font = FONT_Lato_Bold(17.0f);
    lblCityName.font = FONT_Lato_Bold(14.0f);
    lblPositiveCount.font = FONT_Lato_Bold(14.0f);
    lblNeutralCount.font = FONT_Lato_Bold(14.0f);
    lblNegativeCount.font = FONT_Lato_Bold(14.0f);
    lblJoinDate.font = FONT_Lato_Light(14.0f);
    lblVerified.font = FONT_Lato_Light(14.0f);
    lblUserBio.font = FONT_Lato_Bold(14.0f);
    
    BtnFollowers.titleLabel.font = FONT_Lato_Light(14.0f);
    BtnFollowing.titleLabel.font = FONT_Lato_Light(14.0f);
    BtnFollow.titleLabel.font = FONT_Lato_Light(16.0f);
}

// here get profile data...................
- (void) DownLoadProfileData {
    
    NSString * FromUserId;
        @try {
           FromUserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) { }
    
    if([obNet InternetStatus:YES]){
        NSString *url = [NSString stringWithFormat:@"%@&UserId=%@&FollowId=%@",[KAppDelegate getDictServer:WS_ViewProfile],ProductUserId,FromUserId];
        NSLog(@"ViewProfileOther Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json){
            NSLog(@"ViewProfileOther Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    dictProfileData = [json objectForKey:@"UserInformation"];
                    
                    if([obNet isObject:[dictProfileData objectForKey:@"Products"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                        arrItemListing = [dictProfileData objectForKey:@"Products"];
                    }
                    if([obNet isObject:dictProfileData String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
                        lblUserName.text = [dictProfileData objectForKey:@"UserName"];
                        lblHeader.text = [NSString stringWithFormat:@"@%@",[dictProfileData objectForKey:@"UserName"]];
                        
                        NSString * str = [dictProfileData objectForKey:@"UserCityName"];
                        if([str isEqualToString:@""]) {
                            NSString * city = [dictProfileData objectForKey:@"UserDefaultCity"];
                            
                            if([city isEqualToString:@"(null)"] || [city isEqualToString:@"null"]) {
                                lblCityName.text = @"";
                            } else {
                                lblCityName.text = [dictProfileData objectForKey:@"UserDefaultCity"];
                            }
                        } else {
                            lblCityName.text = [dictProfileData objectForKey:@"UserCityName"];
                        }
                        
                        lblPositiveCount.text = [dictProfileData objectForKey:@"UserPositiveFeedBCount"];
                        lblNeutralCount.text = [dictProfileData objectForKey:@"UserNeutralFeedBCount"];
                        lblNegativeCount.text = [dictProfileData objectForKey:@"UserNegativeFeedBCount"];
                        lblJoinDate.text = [NSString stringWithFormat:@"Join date %@",[dictProfileData objectForKey:@"UserRegistationDate"]];
                        lblUserBio.text = [dictProfileData objectForKey:@"UserBio"];
                        
                        NSString * VerificationStatus = [dictProfileData objectForKey:@"UserEmailVerificationStatus"];
                        if(VerificationStatus.intValue == 1) {
                            lblVerified.text = @"Verified";
                        } else {
                            lblVerified.text = @"Not Verified";
                        }
                        
                        NSString * follower = [NSString stringWithFormat:@"%@ followers",[dictProfileData objectForKey:@"UserFollowerCount"]];
                        NSString * following = [NSString stringWithFormat:@"%@ following",[dictProfileData objectForKey:@"UserFollowingCount"]];
                        
                        [BtnFollowers setTitle: follower forState: UIControlStateNormal];
                        [BtnFollowing setTitle: following forState: UIControlStateNormal];
                        
                        
                        NSString  *FollowStatus =[NSString stringWithFormat:@"%d",[[dictProfileData objectForKey:@"FollowStatus"] intValue]];
                        if(FollowStatus.intValue == 1) {
                            [BtnFollow setTitle:@"Following" forState:UIControlStateNormal];
                        } else {
                            [BtnFollow setTitle:@"Follow" forState:UIControlStateNormal];
                        }
                        
                        NSString * strImageUrl =[NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dictProfileData objectForKey:@"UserImage"]];
                        [obNet SetImageToView:imgUserImage fromImageUrl:strImageUrl Option:5];
                        
                        @try {
                            websiteUrl = [NSString stringWithFormat:@"%@",[dictProfileData objectForKey:@"UserWebsiteUrl"]];
                            NSString * url = [websiteUrl stringByReplacingOccurrencesOfString:@"http://" withString:@""];
                            [btnUserWebsiteUrl setTitle:url forState:UIControlStateNormal];
                        } @catch (NSException *exception) { }
                    }
                    [self add_AdjustViewCategoryListing];
                    
                } else {
                    [[[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                }
            }
        }];
    }
}

#pragma mark - Button Actions
- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

// here go to Following screen......................
- (IBAction)BtnFollowing:(id)sender {
    NSString * following;
    
    @try {
        following = [NSString stringWithFormat:@"%@ following",[dictProfileData objectForKey:@"UserFollowingCount"]];
    } @catch (NSException *exception) {}
    
    if(following.intValue > 0) {
        static NSInteger dir = 0;
        Following * follow = [[Following alloc]initWithNibName:@"Following" bundle:nil];
        dir++;
        if(dictProfileData.count > 0) {
            follow.DictProfileData = dictProfileData;
        }
        
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        follow.hidesBottomBarWhenPushed = YES;
        
        [self.navigationController pushViewController:follow animated:YES];
    }
}

// here go to followers screen......................
- (IBAction)BtnFollowers:(id)sender {
    NSString * followerCount;
    
    @try {
        followerCount = [NSString stringWithFormat:@"%@",[dictProfileData objectForKey:@"UserFollowerCount"]];
    } @catch (NSException *exception) {}

    if(followerCount.intValue) {
        static NSInteger dir = 0;
        Followers * follower = [[Followers alloc]initWithNibName:@"Followers" bundle:nil];
        dir++;
        if(dictProfileData.count > 0) {
            follower.DictProfileData = dictProfileData;
        }
        
        KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
        follower.hidesBottomBarWhenPushed = YES;
        
        [self.navigationController pushViewController:follower animated:YES];
    }
}

// here add follow ..........................
- (IBAction)BtnAddFollow:(id)sender {
    NSString * FromUserId =@"";
    NSString * FromUserName =@"";
    NSString * FromUserImage =@"";
    NSString * ToUserId =@"";
    NSString * ToUserName =@"";
    NSString * ToUserImage =@"";
    NSString * StatusValue = @"";
    
    NSString  *FollowStatus =[NSString stringWithFormat:@"%d",[[dictProfileData objectForKey:@"FollowStatus"] intValue]];
    if(FollowStatus.intValue == 1) {
        [BtnFollow setTitle:@"Following" forState:UIControlStateNormal];
        StatusValue = @"0";
    } else {
        [BtnFollow setTitle:@"Follow" forState:UIControlStateNormal];
        StatusValue = @"1";
    }
    [BtnFollow setTitle:@"Loading..." forState:UIControlStateNormal];
    
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
    
    if(dictProfileData.count > 0) {
        @try {
            ToUserId = [dictProfileData objectForKey:@"UserId"];
        } @catch (NSException *exception) {}
        
        @try {
            ToUserName = [dictProfileData objectForKey:@"UserName"];
        } @catch (NSException *exception) {}
        
        @try {
            ToUserImage = [dictProfileData objectForKey:@"UserImage"];
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&FromUserId=%@&FromUserName=%@&FromUserImage=%@&ToUserId=%@&ToUserName=%@&ToUserImage=%@&FollowStatus=%@",[KAppDelegate getDictServer:WS_AddFollow],FromUserId,FromUserName,FromUserImage,ToUserId,ToUserName,ToUserImage,StatusValue];
        NSLog(@"AddFollow Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"AddFollow Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    
                    NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
                    dict = [dictProfileData mutableCopy];
                    [dict setObject:StatusValue forKey:@"FollowStatus"];
                    dictProfileData = dict;
                    NSLog(@"dictProfileData--> %@",dictProfileData);
                    
                    NSString  *FollowStatus =[NSString stringWithFormat:@"%d",[[dictProfileData objectForKey:@"FollowStatus"] intValue]];
                    if(FollowStatus.intValue == 1) {
                        [BtnFollow setTitle:@"Following" forState:UIControlStateNormal];
                    } else {
                        [BtnFollow setTitle:@"Follow" forState:UIControlStateNormal];
                    }
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

// here go to feedback screen.....................
- (IBAction)BtnFeedBack:(id)sender {
    static NSInteger dir = 0;
    FeedBackScreen * feedback = [[FeedBackScreen alloc]initWithNibName:@"FeedBackScreen" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    feedback.hidesBottomBarWhenPushed = YES;
    
    @try {
        NSString * userName = [dictProfileData objectForKey:@"UserName"];
        NSString * UserId = [dictProfileData objectForKey:@"UserId"];
        NSString * UserImage = [dictProfileData objectForKey:@"UserImage"];
        
        NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
        [dict setObject:userName forKey:@"UserName"];
        [dict setObject:UserId forKey:@"UserId"];
        [dict setObject:UserImage forKey:@"UserImage"];
        
        feedback.dictUserInfo = dict;
    } @catch (NSException *exception) {}
    
    [self.navigationController pushViewController:feedback animated:YES];
}

- (IBAction)btnReferesh:(id)sender {
    [self DownLoadProfileData];
}

- (IBAction)btnUserWebsiteUrl:(id)sender {
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:websiteUrl]];
}

#pragma mark - ItemListing Shows
// here set product image to view.................
- (void) add_AdjustViewCategoryListing {
    
    for (UIView *v in scrollView.subviews) {
        [v removeFromSuperview];
    }
    
    CGRect rect = viewFirstInSC.frame;
    rect.origin.x = 0;
    rect.origin.y = 50;
    viewFirstInSC.frame = rect;
    [scrollView addSubview:viewFirstInSC];
    
    int xv, yv, xAxisWidth, yAxisHeight, xPoint, scrollWidth ,yBuffer, xBuffer;
    float fSize;
    
    xv = 8, yv = 340, xAxisWidth = 147, yAxisHeight = 223, xPoint = 320, scrollWidth = 320, xBuffer = 8, yBuffer = 8;
    fSize = 10.0;
    NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
    
    for(int i = 0; i< arrItemListing.count; i++){
        
        dict = [arrItemListing objectAtIndex:i];
        UIView * viewimg = [[UIView alloc] init];
        viewimg.backgroundColor = [UIColor clearColor];
        viewimg.frame = CGRectMake(xv, yv, ViewItems.frame.size.width,ViewItems.frame.size.height);
        viewimg.layer.borderColor = [UIColor colorWithRed:0.733 green:0.733 blue:0.733 alpha:1].CGColor;
        viewimg.layer.borderWidth = 0.5f;
        
        UILabel * lblDivider = [[UILabel alloc]init];
        lblDivider.frame = CGRectMake(lblDividerLine.frame.origin.x, lblDividerLine.frame.origin.y, lblDividerLine.frame.size.width,lblDividerLine.frame.size.height);
        lblDivider.backgroundColor = [UIColor colorWithRed:0.733 green:0.733 blue:0.733 alpha:1];
        
        UIButton * btnrad = [UIButton buttonWithType:UIButtonTypeCustom];
        btnrad.backgroundColor = [UIColor clearColor];
        btnrad.frame = CGRectMake(BtnItem.frame.origin.x, BtnItem.frame.origin.y, BtnItem.frame.size.width,BtnItem.frame.size.height);
        [btnrad setTag:i];
        [btnrad addTarget:self action:@selector(functionToCallButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        
        UIImageView * imgsetImage = [[UIImageView alloc] init];
        imgsetImage.backgroundColor = [UIColor clearColor];
        imgsetImage.frame = CGRectMake(imgBg.frame.origin.x, imgBg.frame.origin.y, imgBg.frame.size.width,imgBg.frame.size.height);
        
        UILabel * lblProductItemName = [[UILabel alloc]init];
        lblProductItemName.backgroundColor = [UIColor clearColor];
        lblProductItemName.textColor = [UIColor blackColor];
        lblProductItemName.font = FONT_Lato_Bold(14.0f);
        lblProductItemName.frame = CGRectMake(lblProductName.frame.origin.x, lblProductName.frame.origin.y, lblProductName.frame.size.width,lblProductName.frame.size.height);
        
        UILabel * lblProductPrice = [[UILabel alloc]init];
        lblProductPrice.backgroundColor = [UIColor clearColor];
        lblProductPrice.textColor = [UIColor blackColor];
        lblProductPrice.font = FONT_Lato_Bold(14.0f);
        lblProductPrice.frame = CGRectMake(lblPrice.frame.origin.x, lblPrice.frame.origin.y, lblPrice.frame.size.width,lblPrice.frame.size.height);
        
        UIImageView * imgLike = [[UIImageView alloc]init];
        imgLike.frame = CGRectMake(imgLikeIcon.frame.origin.x,imgLikeIcon.frame.origin.y, imgLikeIcon.frame.size.width,imgLikeIcon.frame.size.height);
        imgLike.backgroundColor = [UIColor clearColor];
        imgLike.image = [UIImage imageNamed:@"green_like_icon.png"];
        
        UILabel * lblLikeCount = [[UILabel alloc]init];
        lblLikeCount.backgroundColor = [UIColor clearColor];
        lblLikeCount.frame = CGRectMake(lblLikeCountValue.frame.origin.x, lblLikeCountValue.frame.origin.y, lblLikeCountValue.frame.size.width,lblLikeCountValue.frame.size.height);
        lblLikeCount.textColor = [UIColor blackColor];
        lblLikeCount.font = FONT_Lato_Bold(12.0f);
        
        UIButton * BtnLikeClick = [UIButton buttonWithType:UIButtonTypeCustom];
        BtnLikeClick.backgroundColor = [UIColor clearColor];
        BtnLikeClick.frame = CGRectMake(BtnLike.frame.origin.x, BtnLike.frame.origin.y, BtnLike.frame.size.width,BtnLike.frame.size.height);
        [BtnLikeClick addTarget:self action:@selector(BtnLikeClickAction:) forControlEvents:UIControlEventTouchUpInside];
        [BtnLikeClick setTag:i];
        
        // set Divider image here
        UIImageView * imgDivider = [[UIImageView alloc] init];
        imgDivider.frame = CGRectMake(imgGreenDivider.frame.origin.x, imgGreenDivider.frame.origin.y, imgGreenDivider.frame.size.width,imgGreenDivider.frame.size.height);
        imgDivider.backgroundColor = [UIColor blackColor];
        imgDivider.image = [UIImage imageNamed:@"green_divider_category.png"];
        
        UIImageView * imgShare = [[UIImageView alloc]init];
        imgShare.frame = CGRectMake(imgShareIcon.frame.origin.x,imgShareIcon.frame.origin.y, imgShareIcon.frame.size.width,imgShareIcon.frame.size.height);
        imgShare.backgroundColor = [UIColor clearColor];
        imgShare.image = [UIImage imageNamed:@"share_icon_green.png"];
        
        UIButton * BtnShareClick = [UIButton buttonWithType:UIButtonTypeCustom];
        BtnShareClick.backgroundColor = [UIColor clearColor];
        BtnShareClick.frame = CGRectMake(BtnShare.frame.origin.x, BtnShare.frame.origin.y, BtnShare.frame.size.width,BtnShare.frame.size.height);
        [BtnShareClick addTarget:self action:@selector(BtnShareClickAction:) forControlEvents:UIControlEventTouchUpInside];
        [BtnShareClick setTag:i];
        
        // Here we set all data
        
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
                            imgLike.image = [UIImage imageNamed:@"like_red_img.png"];
                            break;
                        } else {
                            imgLike.image = [UIImage imageNamed:@"green_like_icon.png"];
                        }
                    }
                } else {
                    imgLike.image = [UIImage imageNamed:@"green_like_icon.png"];
                }
            }
            
        } @catch (NSException *exception) { }
        
        if([obNet isObject:[dict objectForKey:@"ProductName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            lblProductItemName.text = [dict objectForKey:@"ProductName"];
        }
       
        @try {
            lblProductPrice.text = [NSString stringWithFormat:@"$ %@",[dict objectForKey:@"ProductPrice"]];
        } @catch (NSException *exception) { }
        
        @try {
            lblLikeCount.text = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"ProductLikeCount"] intValue]];
        } @catch (NSException *exception) { }
       
        NSArray * arr = [[NSArray alloc]init];
        if([obNet isObject:[dict objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            arr = [dict objectForKey:@"ProductImageInfo"];
        }
        
        @try {
            if(arr.count > 0) {
                @try {
                    NSDictionary * dict = [arr objectAtIndex:0];
                    strImageShareUrl =[NSString stringWithFormat:@"%@thumbtwo_%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[dict objectForKey:@"ProImageName"]];
                    
                    [imgsetImage GetNSetUIImage:strImageShareUrl DefaultImage:@"no_image.png"];
                    //[obNet SetImageToView:imgsetImage fromImageUrl:strImageShareUrl Option:5];
                } @catch (NSException *exception) {}
            }
        } @catch (NSException *exception) {}
        
        UIImageView * imgSoldProduct = [[UIImageView alloc]init];
        imgSoldProduct.frame = CGRectMake(imgSold.frame.origin.x, imgSold.frame.origin.y, imgSold.frame.size.width,imgSold.frame.size.height);
        imgSoldProduct.image = [UIImage imageNamed:@"sold_img.png"];
        
        @try {
            NSString * slodStatus = [dict objectForKey:@"ProductSoldStatus"];
            if(slodStatus.intValue == 1) {
                imgSoldProduct.hidden = NO;
            } else {
                imgSoldProduct.hidden = YES;
            }
        } @catch (NSException *exception) {}
        
        imgLike.tag=i;
        CGRect rectai = CGRectMake(imgsetImage.frame.origin.x-20+(imgsetImage.frame.size.width/2), imgsetImage.frame.origin.y-20+(imgsetImage.frame.size.height/2), 40, 40);
        UIActivityIndicatorView * ai = [[UIActivityIndicatorView alloc] initWithFrame:rectai];
        [ai setHidden:NO];
        
        [ai setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhiteLarge];
        [ai startAnimating];
        [ai setBackgroundColor:[UIColor clearColor]];
        [ai setColor:[UIColor blackColor]];

        [viewimg addSubview:ai];
        [viewimg addSubview:imgsetImage];
        [viewimg addSubview:lblProductItemName];
        [viewimg addSubview:lblProductPrice];
        [viewimg addSubview:lblDivider];
        [viewimg addSubview:imgLike];
        [viewimg addSubview:lblLikeCount];
        [viewimg addSubview:BtnLikeClick];
        [viewimg addSubview:imgDivider];
        [viewimg addSubview:imgShare];
        [viewimg addSubview:BtnShareClick];
        [viewimg addSubview:imgSoldProduct];
        [viewimg addSubview:btnrad];
        [viewimg setTag:i];
        
        xv = xv + xAxisWidth+ xBuffer;
        
        //To change into down side & again start from previous position of x
        if(xv > xPoint) {
            yv = yv + yAxisHeight + yBuffer;
            xv = 8;
            viewimg.frame = CGRectMake(xv, yv, ViewItems.frame.size.width,ViewItems.frame.size.height);
            xv = xv + xAxisWidth + xBuffer;
        }
        [scrollView addSubview:viewimg];
    }
    [scrollView setContentSize:CGSizeMake(scrollWidth, yv+yAxisHeight+40)];
}

// here we call click on image................
- (void) functionToCallButtonAction: (id) sender {
    NSInteger tag = [sender tag];
    static NSInteger dir = 0;
    ItemDetails * item = [[ItemDetails alloc]initWithNibName:@"ItemDetails" bundle:nil];
    dir++;
    
    if([obNet isObject:arrItemListing String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
        NSDictionary * dict = [arrItemListing objectAtIndex:tag];
        NSString * ProductId = [dict objectForKey:@"ProductId"];
        
        if(ProductId != nil)
            item.ProductId = [NSString stringWithFormat:@"%@",ProductId];
    }
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    item.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:item animated:YES];
}

// here like and unlike webservice.............
- (void) BtnLikeClickAction: (id) sender {
    UIButton *buttonThatWasPressed = (UIButton *)sender;
    buttonThatWasPressed.enabled = NO;
    
    NSString  *ToUserId = @"";
    NSString  *ToUserName = @"";
    NSString  *LikeProductId = @"";
    NSString  *LikeProductName = @"";
    NSString  *LikeStatus = @"";
    NSString  *ProductImage = @"";
    NSString  *FromUserId = @"";
    NSString  *FromUserName = @"";
    NSString  *FromUserImage = @"";
    
    NSInteger tag = [sender tag];
    if (tag >= 1000) {
        tag = tag-1000;
    }
    
    NSMutableDictionary * dictProductDetails = [[NSMutableDictionary alloc]init];
    if(arrItemListing.count > 0) {
        dictProductDetails = [arrItemListing objectAtIndex:tag];
    }
    
    if(KAppDelegate.dictLoginInfo.count > 0){
        @try {
            LikeProductId =[NSString stringWithFormat:@"%d",[[dictProductDetails objectForKey:@"ProductId"] intValue]];
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
    }
 
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
        NSString *url = [NSString stringWithFormat:@"%@&FromUserId=%@&FromUserName=%@&ToUserId=%@&ToUserName=%@&LikeProductId=%@&LikeProductName=%@&LikeStatus=%@&FromUserImage=%@&ProductImage=%@",[KAppDelegate getDictServer:WS_AddLike],FromUserId,FromUserName,ToUserId,ToUserName,LikeProductId,LikeProductName,LikeStatus,FromUserImage,ProductImage];
        NSLog(@"AddLike Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"AddLike Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    [self DownLoadProfileData];
                    buttonThatWasPressed.enabled = YES;
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

//here we share image on socials.......................
- (void) BtnShareClickAction: (id) sender {
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
