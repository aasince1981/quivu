//
//  ItemListingViewController.m
//  Reloved
//
//  Created by Kamal on 19/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "ItemListingViewController.h"
#import "ItemDetails.h"

@interface ItemListingViewController ()
{
    double latdouble;
    double londouble;
    NSString * CategorysId;
    NSString * FromUserId;
    BOOL isSearching;
    int productId;
    int arrIndex;
    BOOL isLiking;
    NSString * sliderValue;
}

@end

@implementation ItemListingViewController
@synthesize DictProductData,enhancedKeyboard,formItems;

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
    
    [self SetFontTypeAndDelegates];
    
    arrItemListing = [[NSMutableArray alloc]init];
    arrCategoryListing = [[NSMutableArray alloc]init];
    arrPopularListing = [[NSMutableArray alloc]init];
    arrPickerData = [[NSMutableArray alloc]init];
    
    [tf_Search setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_MinimumPrice setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_MaximumPrice setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_MinimumPrice];
    [self.formItems addObject:tf_MaximumPrice];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    strImageShareUrl = @"";
    CategorysId = @"";
    FromUserId = @"";
    sliderValue = @"";
    
    slider.minimumValue = 0;
}

// here we set font type and size............
- (void) SetFontTypeAndDelegates {
    tf_Search.delegate = self;
    tf_MinimumPrice.delegate = self;
    tf_MaximumPrice.delegate = self;
    
    scrollView.delegate = self;
    sv_Filter.delegate = self;
    
    picker_mypicker.dataSource = self;
    picker_mypicker.delegate = self;
    
    lblCategory.font = FONT_Lato_Bold(17.0f);
    
    lblSliderValue.font = FONT_Lato_Bold(14.0f);
    lblPopular.font = FONT_Lato_Bold(14.0f);
    lblCategory.font = FONT_Lato_Bold(14.0f);
    lblPriceRange.font = FONT_Lato_Bold(17.0f);
    lblSortBy.font = FONT_Lato_Bold(17.0f);
    lblSearchWithIn.font = FONT_Lato_Bold(17.0f);
    lblfilter.font = FONT_Lato_Bold(13.0f);
}

- (void) viewWillAppear:(BOOL)animated
{
    [btnFilterIcon setBackgroundImage:[UIImage imageNamed:@"filter_icon.png"] forState:UIControlStateNormal];
    KAppDelegate.MyVC = self;
    isSearching=NO;
    if(DictProductData.count > 0) {
        lblCategoryName.text = [DictProductData objectForKey:@"CategoryName"];
    }
    
    CLLocationManager * lManager = [AppDelegate getLoactionManager];
    NSString  * latitude = [NSString stringWithFormat:@"%f", lManager.location.coordinate.latitude];
    NSString  * longitude = [NSString stringWithFormat:@"%f", lManager.location.coordinate.longitude];
    latdouble = [latitude doubleValue];
    londouble = [longitude doubleValue];
    
    if([obNet isObject:DictProductData String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        CategorysId = [DictProductData objectForKey:@"CategoryId"];
    }
    
    if(![CategorysId isEqualToString:@""]) {
        [self GetProductListing];
    }
    
    if(KAppDelegate.dictLoginInfo > 0) {
        @try {
            FromUserId = [NSString stringWithFormat:@"%d",[[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] intValue]];
        } @catch (NSException *exception) { }
    }
    
    [self DownLoadGetCategoriesList];
}

- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_Search.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
    [tf_MinimumPrice.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
    [tf_MaximumPrice.placeholder drawInRect:rect withFont:FONT_Lato_Bold(14.0f)];
}

#pragma mark - Button Actions
- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

// here we search result ..............
- (IBAction)BtnSearch:(id)sender {
    isSearching=YES;
    NSString * UserId = @"";
    if([obNet isObject:KAppDelegate.dictLoginInfo String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        @try {
            UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) { }
    }
    
    if(tf_Search.text.length > 0) {
        if([obNet InternetStatus:YES]) {
            NSString *url = [NSString stringWithFormat:@"%@&searchName=%@&UserId=%@&shortBy=%d&categoryId=%d&searchRange=%@&minPrice=%@&maxPrice=%@&UserLatitude=%f&UserLongitude=%f",[KAppDelegate getDictServer:WS_ProductFilter],tf_Search.text,UserId,PopularId,CategoryId,sliderValue,tf_MinimumPrice.text,tf_MaximumPrice.text,latdouble,londouble];
            NSLog(@"ProductFilter Url-->%@",url);
            
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
                NSLog(@"ProductFilter Json value-->%@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        [tf_Search resignFirstResponder];
                        NSMutableArray * arr = [json objectForKey:@"Products"];
                        arrItemListing = arr;
                        
                        for (UIView * view in scrollView.subviews) {
                            [view removeFromSuperview];
                        }
                        lblCategoryName.text = @"Search Results";
                        [self add_AdjustViewProductListing];
                    } else {
                        UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                        [alert show];
                    }
                }
            }];
        }
    }
}

- (void) handleSingleTap: (id) sender
{
    [view_Filter removeFromSuperview];
}

// here we filter records..........
- (IBAction)BtnFilter:(id)sender {
   // tf_MaximumPrice.text = @"";
    //tf_MinimumPrice.text = @"";
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    
    [singleTap setNumberOfTapsRequired:1];
    [view_TapRemoveFilter addGestureRecognizer:singleTap];
    
    view_Filter.frame = self.view.frame;
    [self.view addSubview:view_Filter];
    
    NSMutableDictionary * d1 = [[NSMutableDictionary alloc]init];
    [d1 setValue:@"Popular" forKey:@"SortBy"];
    [d1 setValue:@"1" forKey:@"id"];
    [arrPopularListing addObject:d1];
    
    NSMutableDictionary * d2 = [[NSMutableDictionary alloc]init];
    [d2 setValue:@"Recent" forKey:@"SortBy"];
    [d2 setValue:@"2" forKey:@"id"];
    [arrPopularListing addObject:d2];
    
    NSMutableDictionary * d3 = [[NSMutableDictionary alloc]init];
    [d3 setValue:@"Nearest" forKey:@"SortBy"];
    [d3 setValue:@"3" forKey:@"id"];
    [arrPopularListing addObject:d3];
    
    NSMutableDictionary * d4 = [[NSMutableDictionary alloc]init];
    [d4 setValue:@"LowPrice" forKey:@"SortBy"];
    [d4 setValue:@"4" forKey:@"id"];
    [arrPopularListing addObject:d4];
    
    NSMutableDictionary * d5 = [[NSMutableDictionary alloc]init];
    [d5 setValue:@"HighPrice" forKey:@"SortBy"];
    [d5 setValue:@"5" forKey:@"id"];
    [arrPopularListing addObject:d5];
}

// here we call filter record webservice............
- (IBAction)BtnRight:(id)sender {
    isSearching=YES;
    NSString * UserId = @"";
    if([obNet isObject:KAppDelegate.dictLoginInfo String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
        @try {
            UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        } @catch (NSException *exception) { }
    }
    
    if(PopularId == 0 && CategoryId == 0 && sliderValue.intValue == 0 && tf_MinimumPrice.text.length == 0 && tf_MaximumPrice.text.length == 0) {
    } else {
        if([obNet InternetStatus:YES]) {
            NSString *url = [NSString stringWithFormat:@"%@&searchName=%@&UserId=%@&shortBy=%d&categoryId=%d&searchRange=%@&minPrice=%@&maxPrice=%@&UserLatitude=%f&UserLongitude=%f",[KAppDelegate getDictServer:WS_ProductFilter],@"",UserId,PopularId,CategoryId,sliderValue,tf_MinimumPrice.text,tf_MaximumPrice.text,latdouble,londouble];
            
            NSLog(@"GetProducts Url-->%@",url);
            [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
                NSLog(@"GetProducts Json value-->%@",json);
                
                if(json != nil) {
                    if([[json objectForKey:@"success"] intValue] == 1) {
                        [btnFilterIcon setBackgroundImage:[UIImage imageNamed:@"filter_icon_right.png"] forState:UIControlStateNormal];
                        [view_Filter removeFromSuperview];
                        NSMutableArray * arr = [json objectForKey:@"Products"];
                        arrItemListing = arr;
                        
                        for (UIView * view in scrollView.subviews) {
                            [view removeFromSuperview];
                        }
                        
                        lblCategoryName.text = @"Search Results";
                        [self add_AdjustViewProductListing];
                    } else {
                        [self Resetvalue];
                        [view_Filter removeFromSuperview];
                        UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                        [alert show];
                    }
                }
            }];
        }
    }
}

#pragma mark - ProductListing Data Load here
- (void) GetProductListing {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&CategoryId=%@&UserLatitude=%f&UserLongitude=%f",[KAppDelegate getDictServer:WS_GetProducts],CategorysId,latdouble,londouble];
        NSLog(@"GetProducts Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetProducts Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    if(IS_IPHONE_5) {
                        [view_PopUp removeFromSuperview];
                    } else {
                        [view_PopUp removeFromSuperview];
                    }
                    
                    if (!isSearching) {
                        arrItemListing = [json objectForKey:@"Products"];
                    }
                    else {
                        for (int i = 0 ; i<arrItemListing.count; i++) {
                            if (productId == [[[arrItemListing objectAtIndex:i] objectForKey:@"ProductId"]intValue]) {
                                NSMutableArray *arr1 = [arrItemListing mutableCopy];
                                NSMutableDictionary *tempDict = [[arr1 objectAtIndex:i]mutableCopy];
                                [arr1 removeObjectAtIndex:arrIndex];
                                NSMutableArray *arrLike = [[tempDict objectForKey:@"ProductLikeInfo"] mutableCopy];
                                int count = (int)arrLike.count;
                                for (int j=0; j<count; j++) {
                                    if (isLiking) {
                                        NSMutableDictionary *dict = [[NSMutableDictionary alloc]init];
                                        //[dict setValue:[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] forKey:@"UserId"];
                                        [dict setObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] forKey:@"UserId"];
                                        [arrLike addObject:dict];
                                
                                        int likeCount = [[tempDict objectForKey:@"ProductLikeCount"] intValue];
                                        likeCount = likeCount + 1;
                                        [tempDict setValue:arrLike forKey:@"ProductLikeInfo"];
                                        [tempDict setValue:[NSString stringWithFormat:@"%d",likeCount] forKey:@"ProductLikeCount"];
                                        break;
                                    } else {
                                        if ([[KAppDelegate.dictLoginInfo objectForKey:@"UserId"]intValue]==[[[arrLike objectAtIndex:j] objectForKey:@"UserId"]intValue]) {
                                            [arrLike removeObjectAtIndex:j];
                                            
                                            int likeCount = [[tempDict objectForKey:@"ProductLikeCount"] intValue];
                                            likeCount = likeCount - 1;
                                            [tempDict setValue:arrLike forKey:@"ProductLikeInfo"];
                                            [tempDict setValue:[NSString stringWithFormat:@"%d",likeCount] forKey:@"ProductLikeCount"];
                                            break;
                                        }
                                    }
                                }
                                if (arrLike.count==0) {
                                    if (isLiking) {
                                        NSMutableDictionary *dict = [[NSMutableDictionary alloc]init];
                                        //[dict setValue:[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] forKey:@"UserId"];
                                        [dict setObject:[KAppDelegate.dictLoginInfo objectForKey:@"UserId"] forKey:@"UserId"];
                                        [arrLike addObject:dict];
                                        int likeCount = [[tempDict objectForKey:@"ProductLikeCount"] intValue];
                                        likeCount = likeCount + 1;
                                        [tempDict setValue:arrLike forKey:@"ProductLikeInfo"];
                                        [tempDict setValue:[NSString stringWithFormat:@"%d",likeCount] forKey:@"ProductLikeCount"];                                   }
                                }
                                [arr1 insertObject:tempDict atIndex:arrIndex];
                                //[arr1 addObject:tempDict];
                                arrItemListing = arr1;
                                break;
                            }
                        }
                    }
                    [self add_AdjustViewProductListing];
                    
                    if(IS_IPHONE_5) {
                        [view_PopUp removeFromSuperview];
                    } else {
                        [view_PopUp removeFromSuperview];
                    }
                } else {
                    arrItemListing = [[NSMutableArray alloc]init];
                    if(IS_IPHONE_5) {
                        view_PopUp.frame = CGRectMake(0, 86, 320, 482);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    } else {
                        view_PopUp.frame = CGRectMake(0, 86, 320, 394);
                        lblMessage.text = [json objectForKey:@"msg"];
                        [self.view addSubview:view_PopUp];
                    }
                }
            } else {
                for (UIView *v in scrollView.subviews) {
                    [v removeFromSuperview];
                }
                [[LoadingViewController instance] stopRotation];
                if(IS_IPHONE_5) {
                    view_PopUp.frame = CGRectMake(0, 86, 320, 482);
                    lblMessage.font = [UIFont systemFontOfSize:17.0];
                    lblMessage.text = [NSString stringWithFormat:@"%@",kCouldnotconnectServer];
                    [self.view addSubview:view_PopUp];
                } else {
                    lblMessage.font = [UIFont systemFontOfSize:17.0];
                    view_PopUp.frame = CGRectMake(0, 86, 320, 394);
                    lblMessage.text = [NSString stringWithFormat:@"%@",kCouldnotconnectServer];
                    [self.view addSubview:view_PopUp];
                }
            }
        }];
    } else {
        for (UIView *v in scrollView.subviews) {
            [v removeFromSuperview];
        }
        [[LoadingViewController instance] stopRotation];
        if(IS_IPHONE_5) {
            view_PopUp.frame = CGRectMake(0, 86, 320, 482);
            lblMessage.font = [UIFont systemFontOfSize:17.0];
            lblMessage.text = [NSString stringWithFormat:@"%@",kInternetNotAvailable];
            [self.view addSubview:view_PopUp];
        } else {
            lblMessage.font = [UIFont systemFontOfSize:17.0];
            view_PopUp.frame = CGRectMake(0, 86, 320, 394);
            lblMessage.text = [NSString stringWithFormat:@"%@",kInternetNotAvailable];
            [self.view addSubview:view_PopUp];
        }
    }
}

#pragma mark - Show CategoryListing
// here we set product item listing to view............
- (void) add_AdjustViewProductListing {
    
    for (UIView * view in scrollView.subviews) {
        [view removeFromSuperview];
    }
    
    int xv, yv, xAxisWidth, yAxisHeight, xPoint, scrollWidth ,yBuffer, xBuffer;
    float fSize;
    
    xv = 8, yv = 10, xAxisWidth = 147, yAxisHeight = 270, xPoint = 320, scrollWidth = 320, xBuffer = 8, yBuffer = 8;
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
        
        UIButton * btnrad = [UIButton buttonWithType:UIButtonTypeCustom];
        btnrad.backgroundColor = [UIColor clearColor];
        btnrad.frame = CGRectMake(btnCategoryView.frame.origin.x, btnCategoryView.frame.origin.y, btnCategoryView.frame.size.width,btnCategoryView.frame.size.height);
        [btnrad setTag:i];
        [btnrad addTarget:self action:@selector(functionToCallButtonAction:) forControlEvents:UIControlEventTouchUpInside];
        
        // set Image Product code here
        UIImageView * imgsetImage = [[UIImageView alloc] init];
        imgsetImage.backgroundColor = [UIColor clearColor];
        //imgsetImage.contentMode = UIViewContentModeScaleAspectFill;
        imgsetImage.frame = CGRectMake(imgBg.frame.origin.x, imgBg.frame.origin.y, imgBg.frame.size.width,imgBg.frame.size.height);
        
        UIImageView * imgHeaderImage = [[UIImageView alloc] init];
        imgHeaderImage.frame = CGRectMake(imgHeader.frame.origin.x, imgHeader.frame.origin.y, imgHeader.frame.size.width,imgHeader.frame.size.height);
        imgHeaderImage.image = [UIImage imageNamed:@"gray_trnsprnt_img.png"];
        
        // set Product Category code here
        UILabel * lblCategoerProductName = [[UILabel alloc]init];
        lblCategoerProductName.backgroundColor = [UIColor clearColor];
        lblCategoerProductName.frame = CGRectMake(lblProductName.frame.origin.x, lblProductName.frame.origin.y, lblProductName.frame.size.width,lblProductName.frame.size.height);
        lblCategoerProductName.textAlignment = NSTextAlignmentLeft;
        lblCategoerProductName.textColor = [UIColor whiteColor];
        lblCategoerProductName.font = FONT_Lato_Bold(15.0f);
        
        UIImageView * imgBottomImage = [[UIImageView alloc] init];
        imgBottomImage.frame = CGRectMake(imgBottom.frame.origin.x, imgBottom.frame.origin.y, imgBottom.frame.size.width,imgBottom.frame.size.height);
        imgBottomImage.image = [UIImage imageNamed:@"gray_trnsprnt_img.png"];
        
        // lblProducrPrice code here
        UILabel * lblProducrPrice = [[UILabel alloc]init];
        lblProducrPrice.backgroundColor = [UIColor clearColor];
        lblProducrPrice.frame = CGRectMake(lblPrice.frame.origin.x, lblPrice.frame.origin.y, lblPrice.frame.size.width,lblPrice.frame.size.height);
        lblProducrPrice.textColor = [UIColor whiteColor];
        lblProducrPrice.font = FONT_Lato_Bold(17.0f);
        
        // set userImage Code here
        UIImageView * imgUserImag = [[UIImageView alloc] init];
        imgUserImag.frame = CGRectMake(imgUserImage.frame.origin.x, imgUserImage.frame.origin.y, imgUserImage.frame.size.width,imgUserImage.frame.size.height);
        imgUserImag.image = [UIImage imageNamed:@"default_user.png"];
        
        // lbl UserName code here
        UILabel * lblUserName = [[UILabel alloc]init];
        lblUserName.backgroundColor = [UIColor clearColor];
        lblUserName.frame = CGRectMake(lblProductUserName.frame.origin.x, lblProductUserName.frame.origin.y, lblProductUserName.frame.size.width,lblProductUserName.frame.size.height);
        lblUserName.textColor = [UIColor darkGrayColor];
        lblUserName.font = FONT_Lato_Bold(12.0f);
        
        // set clock Image here
        UIImageView * imgClockImage = [[UIImageView alloc] init];
        imgClockImage.frame = CGRectMake(imgClock.frame.origin.x, imgClock.frame.origin.y, imgClock.frame.size.width,imgClock.frame.size.height);
        imgClockImage.image = [UIImage imageNamed:@"clock_icon.png"];
        
        // set ProductDuration code here
        UILabel * lblProductDuration = [[UILabel alloc]init];
        lblProductDuration.frame = CGRectMake(lblDuration.frame.origin.x, lblDuration.frame.origin.y, lblDuration.frame.size.width,lblDuration.frame.size.height);
        lblProductDuration.textColor = [UIColor darkGrayColor];
        lblProductDuration.font = FONT_Lato_Bold(10.0f);
        
        // set Background Image on Category Like count here
        UIImageView * imgCateGoryImageLike = [[UIImageView alloc] init];
        imgCateGoryImageLike.frame = CGRectMake(imgCategoryImage.frame.origin.x, imgCategoryImage.frame.origin.y, imgCategoryImage.frame.size.width,imgCategoryImage.frame.size.height);
        imgCateGoryImageLike.image = [UIImage imageNamed:@"category_green_img.png"];
        
        // set Image LikeIcon and Like Count here
        UIImageView * imgLikeIcon = [[UIImageView alloc] init];
        imgLikeIcon.frame = CGRectMake(imgLike.frame.origin.x, imgLike.frame.origin.y, imgLike.frame.size.width,imgLike.frame.size.height);
        //imgLikeIcon.image = [UIImage imageNamed:@"like_white_icon.png"];
        
        @try {
            if([obNet isObject:[dict objectForKey:@"ProductLikeInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
                NSArray * arr = [dict objectForKey:@"ProductLikeInfo"];
                
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
        
        UIButton * btnLike = [UIButton buttonWithType:UIButtonTypeCustom];
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
        
        UIButton * btnShareIcon = [UIButton buttonWithType:UIButtonTypeCustom];
        btnShareIcon.frame = CGRectMake(BtnShare.frame.origin.x, BtnShare.frame.origin.y, BtnShare.frame.size.width,BtnShare.frame.size.height);
        [btnShareIcon addTarget:self action:@selector(BtnShareAction:) forControlEvents:UIControlEventTouchUpInside];
        [btnShareIcon setTag:i];
        
        @try {
            NSString * ProductUserId = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"ProductUserId"] intValue]];
            if(FromUserId.intValue == ProductUserId.intValue) {
                NSString * strImageUrl =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"ProductUserImage"]];
                [obNet SetImageToView:imgUserImag fromImageUrl:strImageUrl Option:5];
            } else {
                NSString * strImageUrl =[NSString stringWithFormat:@"%@thumb_%@",[KAppDelegate.baseurl objectForKey:@"userImagePath"],[dict objectForKey:@"ProductUserImage"]];
                [obNet SetImageToView:imgUserImag fromImageUrl:strImageUrl Option:IV_Save];
            }
        } @catch (NSException *exception) {}
        
        if([obNet isObject:[dict objectForKey:@"ProductName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            lblCategoerProductName.text = [dict objectForKey:@"ProductName"];
        }
        
        @try {
            lblProducrPrice.text = [NSString stringWithFormat:@"$ %@",[dict objectForKey:@"ProductPrice"]];
        } @catch (NSException *exception) { }
        
        @try {
            lblCountLike.text = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"ProductLikeCount"] intValue]];
        } @catch (NSException *exception) { }
        
        if([obNet isObject:[dict objectForKey:@"ProductUserName"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:1]) {
            lblUserName.text = [dict objectForKey:@"ProductUserName"];
        }
        
        @try {
            lblProductDuration.text = [NSString stringWithFormat:@"%@",[KAppDelegate getTimeDiffrece:[dict objectForKey:@"ProductAddDate"]]];
        } @catch (NSException *exception) { }
        
        if([obNet isObject:[dict objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            @try {
                lblCategoerProductName.text = [dict objectForKey:@"ProductName"];
            } @catch (NSException *exception) { }
        }
        
        NSArray * arr = [[NSArray alloc]init];
        if([obNet isObject:[dict objectForKey:@"ProductImageInfo"] String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            arr = [dict objectForKey:@"ProductImageInfo"];
        }
        
        CGRect rectai = CGRectMake(imgsetImage.frame.origin.x-20+(imgsetImage.frame.size.width/2), imgsetImage.frame.origin.y-20+(imgsetImage.frame.size.height/2), 40, 40);
        UIActivityIndicatorView * ai = [[UIActivityIndicatorView alloc] initWithFrame:rectai];
        [ai setHidden:NO];
        
        [ai setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhiteLarge];
        [ai startAnimating];
        [ai setBackgroundColor:[UIColor clearColor]];
        [ai setColor:[UIColor blackColor]];
        
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

// method to call on image click..............
- (void) functionToCallButtonAction: (id) sender {
    @try {
        int tag = [sender tag];
        if (tag >= 1000) {
            tag = tag-1000;
        }
        
        static NSInteger dir = 0;
        ItemDetails * item = [[ItemDetails alloc]initWithNibName:@"ItemDetails" bundle:nil];
        dir++;
        
        if([obNet isObject:arrItemListing String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            NSDictionary * dict = [arrItemListing objectAtIndex:tag];
            NSString * ProductId = [dict objectForKey:@"ProductId"];
            NSString * CategoryName = [NSString stringWithFormat:@"%@",lblCategoryName.text];
            
            if(ProductId != nil)
                item.ProductId = [NSString stringWithFormat:@"%@",ProductId];
        
            @try {
                item.CategoryName = [NSString stringWithFormat:@"%@",CategoryName];
            } @catch (NSException *exception) {}
            
            item.flageBtnBackNavigation = YES;
            
            /*
            NSString * productUserId = [dict objectForKey:@"ProductUserId"];
            
            if([obNet isObject:dict String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
                NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
                if(ProductId != nil) {
                    [dd setObject:ProductId forKey:@"ProductId"];
                }
                
                if(productUserId != nil) {
                    [dd setObject:productUserId forKey:@"ProductUserId"];
                }
                
                if(dict.count > 0) {
                    item.dictProductId = dd;
                }
            } */
            
            KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
            item.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:item animated:YES];
        }
    } @catch (NSException *exception) { }
}

#pragma mark - Button Actions
// Here we call Like Unlike websrvice...............
- (void) BtnLikeAction: (id) sender {
    UIButton *buttonThatWasPressed = (UIButton *)sender;
    buttonThatWasPressed.enabled = NO;
    
    NSString  *FromUserName, *ToUserId, *ToUserName, *LikeProductId, *LikeProductName, *LikeStatus, *FromUserImage, *ProductImage;
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
        productId = [[dict objectForKey:@"ProductId"]intValue];
        arrIndex = tag;
    }
    
    if(dict.count > 0) {
        
        @try {
            LikeProductId =[NSString stringWithFormat:@"%d",[[dict objectForKey:@"ProductId"] intValue]];
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
                            isLiking=NO;
                            LikeStatus = @"0";;
                            break;
                        } else {
                            isLiking=YES;
                            LikeStatus = @"1";
                        }
                    }
                } else {
                    isLiking=YES;
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
                    [self GetProductListing];
                    buttonThatWasPressed.enabled = YES;
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

// here share umage to social...........
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

#pragma mark - pickerView DataSource Method
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    if (boolPopular) {
        return [arrPopularListing count];
    } else if (boolCategory){
        return [arrCategoryListing count];
    }
    return 0;
}

- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if (boolPopular) {
        NSDictionary * dict = [arrPopularListing objectAtIndex:row];
        return [dict objectForKey:@"SortBy"];
    } else if (boolCategory) {
        NSDictionary * dict = [arrCategoryListing objectAtIndex:row];
        return [dict objectForKey:@"CategoryName"];
    }
    
    return 0;
}

- (IBAction)btn_PickerCancel:(id)sender {
    [view_Picker removeFromSuperview];
    [sv_Filter setContentOffset:CGPointMake(0, 0) animated:YES];
}

- (IBAction)btn_PickerDone:(id)sender{
    [view_Picker removeFromSuperview];
    
    if (btnComman == BtnPopular) {
        NSDictionary * dict = [arrPopularListing objectAtIndex:[picker_mypicker selectedRowInComponent:0]];
        if([obNet isObject:dict String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]){
            lblPopular.text = [dict objectForKey:@"SortBy"];
            PopularId = [[dict objectForKey:@"id"] intValue];
        }
        
    } else if (btnComman == BtnCategory){
        NSDictionary * dict = [arrCategoryListing objectAtIndex:[picker_mypicker selectedRowInComponent:0]];
        if([obNet isObject:dict String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]){
            lblCategory.text = [dict objectForKey:@"CategoryName"];
            CategoryId = [[dict objectForKey:@"CategoryId"] intValue];
        }
    }
    
    [sv_Filter setContentOffset:CGPointMake(0, 0) animated:YES];
}

- (IBAction)btn_openPicker:(id)sender{
    [self resignResponder];
    UIButton * btn = (UIButton *) sender;
    btnComman = btn;
    boolCategory = NO;
    boolPopular = NO;
    
    if(btn == BtnPopular){
        boolPopular=YES;
        arrPickerData=arrPopularListing;
        [picker_mypicker reloadAllComponents];
        view_Picker.frame = CGRectMake(0, self.view.frame.size.height-view_Picker.frame.size.height+10, view_Picker.frame.size.width, view_Picker.frame.size.height);
        if ([arrPickerData count] > 0)
            [self.view addSubview:view_Picker];
    } if(btn == BtnCategory){
        boolCategory=YES;
        arrPickerData=arrCategoryListing;
        [picker_mypicker reloadAllComponents];
        view_Picker.frame = CGRectMake(0, self.view.frame.size.height-view_Picker.frame.size.height+10, view_Picker.frame.size.width, view_Picker.frame.size.height);
        if ([arrPickerData count] > 0)
            [self.view addSubview:view_Picker];
    } else{
        [picker_mypicker reloadAllComponents];
        view_Picker.frame = CGRectMake(0, self.view.frame.size.height-view_Picker.frame.size.height+10, view_Picker.frame.size.width, view_Picker.frame.size.height);
        [self.view addSubview:view_Picker];
    }
}

// button to select distance........
- (IBAction)SliderAction:(id)sender {

    NSString * str = [NSString stringWithFormat:@"%f",slider.value];
    NSString * val = [NSString stringWithFormat:@"%d",str.intValue];
    
    if(val.intValue == 0) {
        lblSliderValue.text = @"Countrywide";
        sliderValue = @"";
    } else {
        lblSliderValue.text = [NSString stringWithFormat:@"%dkm from current location",val.intValue];
        sliderValue = [NSString stringWithFormat:@"%d",val.intValue];
    }
}

- (void) Resetvalue {
    tf_MaximumPrice.text = @"";
    tf_MinimumPrice.text = @"";
    lblSliderValue.text = @"Countrywide";
    lblPopular.text = @"Popular";
    lblCategory.text = @"All Category";
    [btnFilterIcon setBackgroundImage:[UIImage imageNamed:@"filter_icon.png"] forState:UIControlStateNormal];
    
    slider.value = 0;
}

- (IBAction)BtnReset:(id)sender {
    [self Resetvalue];
}

// get category listing here.............
-(void) DownLoadGetCategoriesList {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@",[KAppDelegate getDictServer:WS_GetCategories]];
        NSLog(@"GetCategories Url-->%@",url);
        [obNet JSONFromWebServices:url Parameter:nil AI:YES PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetCategories Json value-->%@",json);
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrCategoryListing = [json objectForKey:@"CategoryList"];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

#pragma mark - textfield delegate methods

-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    if( textField == tf_MinimumPrice) {
        double currentValue = [tf_MinimumPrice.text doubleValue];
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
        tf_MinimumPrice.text = [NSString stringWithFormat:@"%.2f", cents / 100.0f];
        
         return NO;
    }
    
    if(textField == tf_MaximumPrice) {
        double currentValue = [tf_MaximumPrice.text doubleValue];
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
        tf_MaximumPrice.text = [NSString stringWithFormat:@"%.2f", cents / 100.0f];
        
         return NO;
    }
    
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    if (tf_Search) {
        [tf_Search resignFirstResponder];
    }
    
    [textField resignFirstResponder];
    [sv_Filter setContentOffset:CGPointMake(0, 0) animated:YES];
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    if(tf_MinimumPrice) {
        if (textField.frame.origin.y >= KeyboardHeight)
            [sv_Filter setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight) animated:YES];
        [tf_MinimumPrice setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
        [tf_MaximumPrice setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
    }
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol
- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        if(tf == tf_MaximumPrice){
            [tf_MaximumPrice resignFirstResponder];
            [sv_Filter setContentOffset:CGPointMake(0, 0) animated:YES];
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
        
        if(tf == tf_MinimumPrice){
            [tf_MinimumPrice resignFirstResponder];
            [sv_Filter setContentOffset:CGPointMake(0, 0) animated:YES];
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
            [sv_Filter setContentOffset:CGPointMake(0, 0) animated:YES];
            break;
        }
    }
}

- (void) resignResponder {
    [tf_MaximumPrice resignFirstResponder];
    [tf_MinimumPrice resignFirstResponder];
    [tf_Search resignFirstResponder];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
