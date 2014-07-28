//
//  BrowseScreen.m
//  Reloved
//
//  Created by Kamal on 15/05/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "BrowseScreen.h"
#import "ItemListingViewController.h"
#import "FindAndInviteFriends.h"

@interface BrowseScreen ()
    
@end

@implementation BrowseScreen

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
    arrCategoryList = [[NSMutableArray alloc]init];
    [self.navigationController setNavigationBarHidden:YES];
}

#pragma mark - viewWillAppear Methods
- (void) viewWillAppear:(BOOL)animated
{
    [self DownLoadGetCategoriesList];
    KAppDelegate.MyVC = self;
}

- (void) viewWillAppearClone {
    [[LoadingViewController instance] startRotation];
    [self DownLoadGetCategoriesList];
}

// here we call Category Listing webservice........
-(void) DownLoadGetCategoriesList {
    if([obNet InternetStatus:YES]) {
        if(IS_IPHONE_5) {
            [view_PopUp removeFromSuperview];
        } else {
            [view_PopUp removeFromSuperview];
        }
        
        NSString *url = [NSString stringWithFormat:@"%@",[KAppDelegate getDictServer:WS_GetCategories]];
        NSLog(@"GetCategories Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"GetCategories Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrCategoryList = [json objectForKey:@"CategoryList"];
                    [self add_AdjustViewCategoryListing];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
                 [[LoadingViewController instance] stopRotation];
            } else {
                for (UIView *v in scrollView.subviews) {
                    [v removeFromSuperview];
                }
                
                [[LoadingViewController instance] stopRotation];
                if(IS_IPHONE_5) {
                    view_PopUp.frame = CGRectMake(0, 0, 320, scrollView.frame.size.height);
                    lblMessage.font = [UIFont systemFontOfSize:17.0];
                    lblMessage.text = [NSString stringWithFormat:@"%@",kCouldnotconnectServer];
                    [scrollView addSubview:view_PopUp];
                } else {
                    lblMessage.font = [UIFont systemFontOfSize:17.0];
                    view_PopUp.frame = CGRectMake(0, 0, 320, scrollView.frame.size.height);
                    lblMessage.text = [NSString stringWithFormat:@"%@",kCouldnotconnectServer];
                    [scrollView addSubview:view_PopUp];
                }
            }
        }];
    } else {
        for (UIView *v in scrollView.subviews) {
            [v removeFromSuperview];
        }
        
        [[LoadingViewController instance] stopRotation];
        if(IS_IPHONE_5) {
            view_PopUp.frame = CGRectMake(0, 0, 320, scrollView.frame.size.height);
            lblMessage.font = [UIFont systemFontOfSize:17.0];
            lblMessage.text = [NSString stringWithFormat:@"%@",kInternetNotAvailable];
            [scrollView addSubview:view_PopUp];
        } else {
            lblMessage.font = [UIFont systemFontOfSize:17.0];
            view_PopUp.frame = CGRectMake(0, 0, 320, scrollView.frame.size.height);
            lblMessage.text = [NSString stringWithFormat:@"%@",kInternetNotAvailable];
            [scrollView addSubview:view_PopUp];
        }
    }
}

// gallery image scrolling
- (void) add_AdjustViewCategoryListing {
    int xv, yv, xAxisWidth, yAxisHeight, xPoint, scrollWidth ,yBuffer, xBuffer;
    float fSize;
    
    xv = 8, yv = 8, xAxisWidth = 146, yAxisHeight = xAxisWidth, xPoint = 320, scrollWidth = 320, xBuffer = 8, yBuffer = 8;
    fSize = 10.0;
    
    for(int i = 0; i< arrCategoryList.count; i++){
        NSDictionary * dict = [arrCategoryList objectAtIndex:i];
        
        UIView * viewimg = [[UIView alloc] init];
        viewimg.backgroundColor = [UIColor clearColor];
        viewimg.frame = CGRectMake(xv, yv, view_CategoryList.frame.size.width,view_CategoryList.frame.size.height);
        
        UIButton * btnrad = [UIButton buttonWithType:UIButtonTypeCustom];
        btnrad.backgroundColor = [UIColor clearColor];
        btnrad.frame = CGRectMake(btnCategoryView.frame.origin.x, btnCategoryView.frame.origin.y, btnCategoryView.frame.size.width,btnCategoryView.frame.size.height);
        [btnrad setTag:i];
        [btnrad addTarget:self action:@selector(functionToCallButtonAction:) forControlEvents:UIControlEventTouchUpInside];
       
        UIImageView * imgsetImage = [[UIImageView alloc] init];
        imgsetImage.backgroundColor = [UIColor clearColor];
        imgsetImage.frame = CGRectMake(imgCategoryView.frame.origin.x, imgCategoryView.frame.origin.y, imgCategoryView.frame.size.width,imgCategoryView.frame.size.height);
        
        UILabel * lbl = [[UILabel alloc]init];
        lbl.backgroundColor = [UIColor clearColor];
        lbl.textColor = [UIColor whiteColor];
        lbl.font = FONT_Lato_Bold(14.0f);
        lbl.frame = CGRectMake(lblCategoryName.frame.origin.x, lblCategoryName.frame.origin.y, lblCategoryName.frame.size.width,lblCategoryName.frame.size.height);
        
        UIImageView * imgBg = [[UIImageView alloc]init];
        imgBg.frame = CGRectMake(imgTextBg.frame.origin.x,imgTextBg.frame.origin.y, imgTextBg.frame.size.width,imgTextBg.frame.size.height);
        imgBg.backgroundColor = [UIColor clearColor];
        imgBg.image = [UIImage imageNamed:@"gray_trnsprnt_img.png"];
    
        CGRect rectai = CGRectMake(imgsetImage.frame.origin.x-20+(imgsetImage.frame.size.width/2), imgsetImage.frame.origin.y-20+(imgsetImage.frame.size.height/2), 40, 40);
        UIActivityIndicatorView * ai = [[UIActivityIndicatorView alloc] initWithFrame:rectai];
        [ai setHidden:NO];
        
        [ai setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleWhiteLarge];
        [ai startAnimating];
        [ai setBackgroundColor:[UIColor clearColor]];
        [ai setColor:[UIColor blackColor]];
        
        @try {
            NSString * str =[NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"categoryImagePath"],[dict objectForKey:@"CategoryImage"]];
            lbl.text = [dict objectForKey:@"CategoryName"];
            [imgsetImage GetNSetUIImage:str DefaultImage:@"no_image.png"];
        } @catch (NSException *exception) {}
    
        imgsetImage.tag = i;
    
        [viewimg addSubview:ai];
        [viewimg addSubview:imgsetImage];
        [viewimg addSubview:imgBg];
        [viewimg addSubview:lbl];
        [viewimg addSubview:btnrad];
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

// Fuction call click on category image........
- (void) functionToCallButtonAction: (id) sender {
    @try {
        int tag = [sender tag];
        //if (tag >= 1000) {
            //tag = tag-1000;
        //}
        
        static NSInteger dir = 0;
        ItemListingViewController * Item = [[ItemListingViewController alloc]initWithNibName:@"ItemListingViewController" bundle:nil];
        dir++;
        
        if([obNet isObject:arrCategoryList String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:2]) {
            NSDictionary * dict = [arrCategoryList objectAtIndex:tag];
            if([obNet isObject:dict String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]) {
                Item.DictProductData = [dict mutableCopy];
            }
            
            KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
            Item.hidesBottomBarWhenPushed = YES;
            
            [self.navigationController pushViewController:Item animated:YES];
        }
    } @catch (NSException *exception) { }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (IBAction)btnUserPlusIcon:(id)sender {
    static NSInteger dir = 0;
    FindAndInviteFriends * find = [[FindAndInviteFriends alloc]initWithNibName:@"FindAndInviteFriends" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    find.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:find animated:YES];
}

- (IBAction)btnSearchIcon:(id)sender {
    static NSInteger dir = 0;
    ItemListingViewController * Item = [[ItemListingViewController alloc]initWithNibName:@"ItemListingViewController" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    Item.hidesBottomBarWhenPushed = YES;
    
    [self.navigationController pushViewController:Item animated:YES];
}

- (IBAction)btnReferesh:(id)sender {
    [[LoadingViewController instance] startRotation];
    [self DownLoadGetCategoriesList];
}

@end
