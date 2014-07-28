//
//  EditPhotoScreen.m
//  quivu
//
//  Created by Kamal on 19/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "EditItemScreen.h"
#import "NearByViewController.h"
#import "CameraTest.h"
#import "MeViewController.h"
#import "ItemDetails.h"

@interface EditItemScreen ()
{
    NSInteger tag;
    NSString * FromUserId;
    NSString * FromUserName;
    NSString * FromUserImage;
}

@end

#define CurrentButton @"currentbutton"
@implementation EditItemScreen
@synthesize enhancedKeyboard,formItems,sv_ScrollView,imgProduct,dictProductInfo;

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
    
    [self SetFontType];
    
    tf_Price.delegate= self;
    tf_ItemName.delegate= self;
    tv_Message.delegate= self;
    
    [tf_CateGoryName setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_SellingItemName setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_Price setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    [tf_ItemName setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_Price];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
    
    arrCategoryListing = [[NSMutableArray alloc] init];
    arrPickerData = [[NSMutableArray alloc] init];
    picker_mypicker.dataSource = self;
    picker_mypicker.delegate = self;
}

//here we set textfield font and size.........
- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_CateGoryName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_SellingItemName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_Price.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_ItemName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
}

//here we set font and size.........
- (void) SetFontType {
    lblSell.font = FONT_Lato_Bold(20.0f);
    lblCoverPhoto.font = FONT_Lato_Bold(13.0f);
    lblDetails.font = FONT_Lato_Bold(17.0f);
    lblCategory.font = FONT_Lato_Light(17.0f);
    lblItem.font = FONT_Lato_Light(17.0f);
    lblPrice.font = FONT_Lato_Light(17.0f);
    btnNearByPlace.titleLabel.font = FONT_Lato_Bold(14.0f);
}

#pragma mark - viewWillAppear methods
- (void) viewWillAppear:(BOOL)animated {
    [tv_Message setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
    KAppDelegate.MyVC = self;
    
    if(KAppDelegate.DictEditProductInfo.count > 0) {
        dictProductInfo = KAppDelegate.DictEditProductInfo;
    }
    
    [self GetCategoriesList];
    //[self SetProductDetails];
    
    if (imgProduct) {
        if (KAppDelegate.dictImages) {
            NSString * currentbutton = KAppDelegate.dictImages[CurrentButton];
            
            if (currentbutton) {
                NSString * url = [self SaveUIImage:imgProduct];
                
                if (url)
                    KAppDelegate.dictImages[currentbutton] = url;
            }
        }
    }
    
    [self SetAllButtonImages];
    [self SetProductDetails];
    
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
    
    if(KAppDelegate.NearByPlace != nil)
        [btnNearByPlace setTitle:KAppDelegate.NearByPlace forState:UIControlStateNormal];
}

//here we set product image on buttons........
- (void) SetAllButtonImages
{
    [self performSelectorInBackground:@selector(SetAllButtonImagesThread) withObject:nil];
}

- (void) SetAllButtonImagesThread
{
    for (int i = 2; i <= 4; i++) {
        NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%d", i]];
        if (str) {
            if (str.length > 0) {
                
                UIImage * img = [self GetSavedFile:str];
                if (img) {
                    switch (i) {
                        case 2:
                            imgProduct2.image = img;
                            break;
                        case 3:
                            imgProduct3.image = img;
                            break;
                        case 4:
                            imgProduct4.image = img;
                            break;
                    }
                }
            }
        }
    }
}

- (UIImage *) GetSavedFile:(NSString *) url
{
    NSString * imgFileName = [NSString stringWithFormat:@"%@", [url imageNameFromUrl]];
    NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString * documentsDirec = [paths objectAtIndex:0];
    NSString * savedPath = [documentsDirec stringByAppendingPathComponent:imgFileName];
    UIImage * image = [UIImage imageWithContentsOfFile:savedPath];
    
    return image;
}

- (NSString *) SaveUIImage:(UIImage *) image
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy_MM_dd_hh_mm_ss"];
    
    NSDate *date = [NSDate date];
    NSString *convertedString = [dateFormatter stringFromDate:date];
    NSLog(@"Converted String : %@",convertedString);
    
    NSData * data = UIImageJPEGRepresentation(image, 1.0);
    
    NSArray * paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString * basePath = ([paths count] > 0) ? [paths objectAtIndex:0] : nil;
    
    NSString * filePath = [basePath stringByAppendingPathComponent:[NSString stringWithFormat:@"%@.jpg", convertedString]];
    [data writeToFile:filePath atomically:YES];
    
    return filePath;
}

// here we set product details to edit item........
- (void) SetProductDetails {
    if (dictProductInfo.count > 0) {
        
        @try {
            tf_CateGoryName.text = [dictProductInfo objectForKey:@"CategoryName"];
        } @catch (NSException *exception) {}
        
        @try {
            tf_SellingItemName.text = [dictProductInfo objectForKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            tf_Price.text =[NSString stringWithFormat:@"$ %@",[dictProductInfo objectForKey:@"ProductPrice"]];
        } @catch (NSException *exception) {}
        
        @try {
            tf_ItemName.text = [dictProductInfo objectForKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            if([[dictProductInfo objectForKey:@"ProductDescription"] isEqualToString:@""] || [[dictProductInfo objectForKey:@"ProductDescription"] isEqualToString:@"null"]) {
                 tv_Message.text= @"(Optional)                                                     Describe more about what you are selling.";
            } else {
                tv_Message.text = [dictProductInfo objectForKey:@"ProductDescription"];
            }
        } @catch (NSException *exception) {}
        
        @try {
            if([[dictProductInfo objectForKey:@"ProductAddress"] isEqualToString:@""] || [[dictProductInfo objectForKey:@"ProductAddress"] isEqualToString:@"(null)"]) {
                [btnNearByPlace setTitle:@"Specify a place where you prefer to meet buyer." forState:UIControlStateNormal];
            } else {
                [btnNearByPlace setTitle:[dictProductInfo objectForKey:@"ProductAddress"] forState:UIControlStateNormal];
            }
            
        } @catch (NSException *exception) {}
        
        @try {
            NSArray * arr = [dictProductInfo objectForKey:@"ProductImageInfo"];
            if(arr.count > 0) {
                @try {
                    if(KAppDelegate.imgCoverPhoto != nil) {
                        [imgProduct1 setImage:KAppDelegate.imgCoverPhoto];
                    } else {
                        NSString * Url1 =[NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arr objectAtIndex:0] objectForKey:@"ProImageName"]];
                        [obNet SetImageToView:imgProduct1 fromImageUrl:Url1 Option:5];
                    }
                } @catch (NSException *exception) {}
            }
            
            if(arr.count > 1) {
                NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%d", 2]];
                if (str) {
                    if (str.length > 0) {
                        UIImage * img = [self GetSavedFile:str];
                        imgProduct2.image = img;
                    }
                } else {
                    if(KAppDelegate.dictDeleteProductId.count > 0) {
                        NSString * str1 = [KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId1"];
                        NSString * str2 = [[arr objectAtIndex:1] objectForKey:@"ProImageId"];
                        
                        if([str1 isEqualToString:str2]) {
                            imgProduct2.image = [UIImage imageNamed:@"camera_add_btn.png"];
                        }
                    } else {
                        NSString * Url2 =[NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arr objectAtIndex:1] objectForKey:@"ProImageName"]];
                        [obNet SetImageToView:imgProduct2 fromImageUrl:Url2 Option:5];
                    }
                }
            }
            
            if(arr.count > 2) {
                NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%d", 3]];
                if (str) {
                    if (str.length > 0) {
                        UIImage * img = [self GetSavedFile:str];
                        imgProduct3.image = img;
                    }
                } else {
                    if(KAppDelegate.dictDeleteProductId.count > 0) {
                        NSString * str1 = [KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId2"];
                        NSString * str2 = [[arr objectAtIndex:2] objectForKey:@"ProImageId"];
                        
                        if([str1 isEqualToString:str2]) {
                            imgProduct3.image = [UIImage imageNamed:@"camera_add_btn.png"];
                        }
                    } else {
                        NSString * Url3 =[NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arr objectAtIndex:2] objectForKey:@"ProImageName"]];
                        [obNet SetImageToView:imgProduct3 fromImageUrl:Url3 Option:5];
                    }
                }
            }
            
            if(arr.count > 3) {
                NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%d", 4]];
                if (str) {
                    if (str.length > 0) {
                        UIImage * img = [self GetSavedFile:str];
                        imgProduct4.image = img;
                    }
                } else {
                    if(KAppDelegate.dictDeleteProductId.count > 0) {
                        NSString * str1 = [KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId3"];
                        NSString * str2 = [[arr objectAtIndex:3] objectForKey:@"ProImageId"];
                        
                        if([str1 isEqualToString:str2]) {
                            imgProduct4.image = [UIImage imageNamed:@"camera_add_btn.png"];
                        }
                    } else {
                        NSString * Url4 =[NSString stringWithFormat:@"%@%@",[KAppDelegate.baseurl objectForKey:@"productImagePath"],[[arr objectAtIndex:3] objectForKey:@"ProImageName"]];
                        [obNet SetImageToView:imgProduct4 fromImageUrl:Url4 Option:5];
                    }
                }
            }
        } @catch (NSException *exception) {}
    }
}

#pragma mark - CategoryListing
// here we get category listing here...........
-(void) GetCategoriesList {
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@",[KAppDelegate getDictServer:WS_GetCategories]];
        NSLog(@"GetCategories Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            //NSLog(@"GetCategories Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrCategoryListing = [json objectForKey:@"CategoryList"];
                    
                    if(dictProductInfo.count > 0) {
                        for(int i=0;i<arrCategoryListing.count; i++) {
                            NSDictionary * dict = [arrCategoryListing objectAtIndex:i];
                            NSString * CategoryName = [dictProductInfo objectForKey:@"CategoryName"];
                            NSString * ProductCatId = [dictProductInfo objectForKey:@"ProductCatId"];
                            NSString * CatId = [dict objectForKey:@"CategoryId"];
                           // NSLog(@"ProductCatId %@,CatId%@",ProductCatId,CatId);
                            
                            if(ProductCatId.intValue == CatId.intValue) {
                                NSString * str = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"CategoryId"] intValue]];
                                CategoryId = [str intValue];
                                tf_CateGoryName.text = [dict objectForKey:@"CategoryName"];
                                break;
                            }
                            
                            if([CategoryName isEqualToString:[dict objectForKey:@"CategoryName"]]) {
                                NSString * str = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"CategoryId"] intValue]];
                                CategoryId = [str intValue];
                                break;
                            }
                        }
                    }
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

#pragma mark - Button actions

- (IBAction)btnItem:(id)sender {
    [tf_Price resignFirstResponder];
    sv_ScrollView.contentSize = CGSizeMake(0,0);
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_ItemName];
    [self.formItems addObject:tv_Message];
    view_MyView.frame = self.view.frame;
    [self.view addSubview:view_MyView];
}

// here we call on product item image click to add or edit image.......
- (IBAction)BtnAddProductImage:(id)sender {
    UIButton * btn = (UIButton *) sender;
    tag = btn.tag;
    
    NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
    dict = [KAppDelegate.DictEditProductInfo mutableCopy];
    NSString * price = [NSString stringWithFormat:@"%@",tf_Price.text];
    if(dict.count > 0) {
        @try {
            [dict setObject:price forKey:@"ProductPrice"];
        } @catch (NSException *exception) {}
    }
    
    KAppDelegate.boolPopController = YES;
    
    if (!KAppDelegate.dictImages)
        KAppDelegate.dictImages = [[NSMutableDictionary alloc] init];
    
    KAppDelegate.dictImages[CurrentButton] = [NSString stringWithFormat:@"%ld", (long)btn.tag];
    
    static NSInteger dir = 0;
    CameraTest * camera = [[CameraTest alloc]initWithNibName:@"CameraTest" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    camera.hidesBottomBarWhenPushed = NO;
    
    if(btn.tag == 1) {
        KAppDelegate.boolImageSelect = NO;
        
        if([imgProduct1.image isEqual:[UIImage imageNamed:@"camera_add_btn.png"]]) {
            KAppDelegate.boolImage1 = YES;
            [self.navigationController pushViewController:camera animated:NO];
        } else {
            imgEdit.frame = CGRectMake(imgDelete.frame.origin.x, imgEdit.frame.origin.y, imgEdit.frame.size.width, imgEdit.frame.size.height);
            btnEdit.frame = CGRectMake(btnDelete.frame.origin.x, btnEdit.frame.origin.y, btnEdit.frame.size.width, btnEdit.frame.size.height);
            
            imgDelete.hidden = YES;
            btnDelete.hidden = YES;
            view_EditPhoto.frame = self.view.frame;
            img_EditPhoto.image = imgProduct1.image;
            
            [self.view addSubview:view_EditPhoto];
        }
    }
    
    if(btn.tag == 2) {
        KAppDelegate.boolImageSelect = YES;
        
        if([imgProduct2.image isEqual:[UIImage imageNamed:@"camera_add_btn.png"]]) {
            KAppDelegate.boolImage2 = YES;
            [self.navigationController pushViewController:camera animated:NO];
        } else {
            imgEdit.frame = CGRectMake(51, imgEdit.frame.origin.y, imgEdit.frame.size.width, imgEdit.frame.size.height);
            btnEdit.frame = CGRectMake(51, btnEdit.frame.origin.y, btnEdit.frame.size.width, btnEdit.frame.size.height);
            
            imgDelete.hidden = NO;
            btnDelete.hidden = NO;
            
            view_EditPhoto.frame = self.view.frame;
            img_EditPhoto.image = imgProduct2.image;
            [self.view addSubview:view_EditPhoto];
        }
    }
    
    if(btn.tag == 3) {
        KAppDelegate.boolImageSelect = YES;
        
        if ([imgProduct3.image isEqual:[UIImage imageNamed:@"camera_add_btn.png"]]){
            KAppDelegate.boolImage3 = YES;
            [self.navigationController pushViewController:camera animated:NO];
        } else {
            imgEdit.frame = CGRectMake(51, imgEdit.frame.origin.y, imgEdit.frame.size.width, imgEdit.frame.size.height);
            btnEdit.frame = CGRectMake(51, btnEdit.frame.origin.y, btnEdit.frame.size.width, btnEdit.frame.size.height);
            
            imgDelete.hidden = NO;
            btnDelete.hidden = NO;
            
            view_EditPhoto.frame = self.view.frame;
            img_EditPhoto.image = imgProduct3.image;
            [self.view addSubview:view_EditPhoto];
        }
    }
    
    if(btn.tag == 4) {
        KAppDelegate.boolImageSelect = YES;

        if ([imgProduct4.image isEqual:[UIImage imageNamed:@"camera_add_btn.png"]]){
             KAppDelegate.boolImage4 = YES;
            [self.navigationController pushViewController:camera animated:NO];
        } else {
            imgEdit.frame = CGRectMake(51, imgEdit.frame.origin.y, imgEdit.frame.size.width, imgEdit.frame.size.height);
            btnEdit.frame = CGRectMake(51, btnEdit.frame.origin.y, btnEdit.frame.size.width, btnEdit.frame.size.height);
            
            imgDelete.hidden = NO;
            btnDelete.hidden = NO;
            
            view_EditPhoto.frame = self.view.frame;
            img_EditPhoto.image = imgProduct4.image;
            [self.view addSubview:view_EditPhoto];
        }
    }
}

- (IBAction)BtnBack:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

// button to update items.....
- (IBAction)BtnRight:(id)sender {
    [tf_Price resignFirstResponder];
    [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    
    NSString * UserId = @"";
    NSString * UserName = @"";
    NSString * UserImage = @"";
    NSString * latitude = @"";
    NSString * longitude = @"";
    NSString * productId = @"";
    NSString * address = @"";
    
     NSMutableArray *aarr=[NSMutableArray new];
    if (KAppDelegate.dictDeleteProductId.count) {
        NSString * str1;
        NSString * str2;
        NSString * str3;
        
        if([KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId1"] != nil) {
            str1 = [KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId1"];
            if(str1.length > 0)
                [aarr addObject:str1];
        }
        
        if([KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId2"] != nil) {
            str2 = [KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId2"];
            if(str2.length > 0)
                [aarr addObject:str2];
        }
        
        if([KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId3"] != nil) {
            str3 = [KAppDelegate.dictDeleteProductId objectForKey:@"ProImageId3"];
            if(str3.length > 0)
                [aarr addObject:str3];
        }
    }
   
    NSMutableString *DeleteId= [NSMutableString new];
    for (int i=0; i<[aarr count]; i++) {
        NSString * str  = [NSString stringWithFormat:@"%@", [aarr objectAtIndex:i]];
        [DeleteId appendString:[NSString stringWithFormat:@"%@,",str]];
    }
    
    if(DeleteId.length>0) {
        [DeleteId deleteCharactersInRange:(NSRange){[DeleteId length] - 1, 1}];
    } else {
        DeleteId = [NSMutableString new];
    }
    
    if(KAppDelegate.dictLoginInfo.count > 0) {
        UserId = [KAppDelegate.dictLoginInfo objectForKey:@"UserId"];
        UserName = [KAppDelegate.dictLoginInfo objectForKey:@"UserName"];
        UserImage = [KAppDelegate.dictLoginInfo objectForKey:@"UserImage"];
    }
    
    @try {
        productId = [dictProductInfo objectForKey:@"ProductId"];
    } @catch (NSException *exception) {}
    
    address = [NSString stringWithFormat:@"%@",btnNearByPlace.titleLabel.text];
    
    CLLocationManager * lManager = [AppDelegate getLoactionManager];
    latitude = [NSString stringWithFormat:@"%f", lManager.location.coordinate.latitude];
    longitude = [NSString stringWithFormat:@"%f", lManager.location.coordinate.longitude];
    
    NSString * msg = nil;
    if(tf_CateGoryName.text.length == 0) {
        msg = @"Please select category";
    } else if (tf_SellingItemName.text.length == 0) {
        msg = @"Please enter item name";
    } else if (tf_Price.text.length == 0) {
        msg  = @"Please enter price";
    }
    
    if(msg) {
        [obNet PopUpMSG:msg Header:@""];
    } else {
        if([obNet InternetStatus:YES]) {
            NSString * price = [NSString stringWithFormat:@"%@",tf_Price.text];
            if(price.length > 0) {
                price = [price stringByReplacingOccurrencesOfString:@"$ " withString:@""];
            }
            
            NSString * webservice;
            webservice = [NSString stringWithFormat:@"%@&ProductCatName=%@&ProductCatId=%d&ProductPrice=%@&ProductName=%@&ProductDescription=%@&ProductLatitude=%@&ProductLongitude=%@&ProductUserId=%@&ProductUserName=%@&ProductUserImage=%@&ProductAddress=%@&ProductId=%@&ProductDeleteImage=%@",[KAppDelegate getDictServer:WS_EditProduct],tf_CateGoryName.text,CategoryId,price,tf_SellingItemName.text,tv_Message.text,latitude,longitude,UserId,UserName,UserImage,address,productId,DeleteId];
            NSLog(@"EditProduct Url-->%@",webservice);
            
            NSString * fileName = @"harish.jpg";
            NSData *fileData2;
            NSData *fileData3;
            NSData *fileData4;
            
            [[LoadingViewController instance] startRotation];
            
            UIImage * imgProfile1 = imgProduct1.image;
            NSData *fileData1 = UIImageJPEGRepresentation(imgProfile1, 0.2);
            
            if (imgProduct2.image != [UIImage imageNamed:@"camera_add_btn.png"]){
                UIImage * imgProfile2 = imgProduct2.image;
                fileData2 = UIImageJPEGRepresentation(imgProfile2, 0.2);
            }
            
            if (imgProduct3.image != [UIImage imageNamed:@"camera_add_btn.png"]){
                UIImage * imgProfile3 = imgProduct3.image;
                fileData3 = UIImageJPEGRepresentation(imgProfile3, 0.2);
            }
            
            if (imgProduct4.image != [UIImage imageNamed:@"camera_add_btn.png"]){
                UIImage * imgProfile4 = imgProduct4.image;
                fileData4 = UIImageJPEGRepresentation(imgProfile4, 0.2);
            }
            
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
            
            // here we upload image to server.......
            if (KAppDelegate.boolImage1 == YES) {
                if (fileData1) {
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage1\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:fileData1];
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                }
            }
            
            if (KAppDelegate.boolImage2 == YES) {
                if (fileData2) {
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage2\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:fileData2];
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                }
            }
            
            if (KAppDelegate.boolImage3 == YES) {
                if (fileData3) {
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage3\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                    
                    [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:fileData3];
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                }
            }
            
            if (KAppDelegate.boolImage4 == YES) {
                if (fileData4) {
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage4\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                    
                    [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                    [body appendData:fileData4];
                    [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                }
            }
            
            [request setHTTPBody:body];
            
            @try {
                NSURLResponse *response;
                NSData *POSTReply = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:nil];
                NSString *theReply = [[NSString alloc] initWithBytes:[POSTReply bytes] length:[POSTReply length] encoding: NSASCIIStringEncoding];
                
                NSLog(@"theReply-%@", theReply);
                
                NSError * e;
                NSMutableDictionary *dictJSON = [NSJSONSerialization JSONObjectWithData: [theReply dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers error: &e];
                NSLog(@"AddProduct Url-->%@",webservice);
                
                NSLog(@"UploadFileWithFileDataJSON = %@, e-%@", dictJSON, e);
                
                if (dictJSON != nil) {
                    if ([[dictJSON objectForKey:@"success"]intValue] == 1) {
                        
                        [[[UIAlertView alloc]initWithTitle:@"" message:[dictJSON objectForKey:@"msg"] delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil] show];
                        
                    } else {
                        [obNet PopUpMSG:[dictJSON objectForKey:@"msg"] Header:@""];
                    }
                }
                [[LoadingViewController instance] stopRotation];
            } @catch (NSException *exception) {}
        }
    }
}

// here we call on succefully add item PopUp............
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(buttonIndex == 0) {
        KAppDelegate.boolForEditProduct = NO;
        KAppDelegate.DictEditProductInfo = [[NSMutableDictionary alloc]init];
        KAppDelegate.boolImageSelect = NO;
        KAppDelegate.dictImages = [[NSMutableDictionary alloc]init];
        [KAppDelegate TabBarShow];
    }
}

#pragma mark - pickerView DataSource Method
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (boolCategory) {
        return [arrCategoryListing count];
    }
    
    return 0;
}

- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if (boolCategory){
        NSDictionary * dict = [arrCategoryListing objectAtIndex:row];
        return [dict objectForKey:@"CategoryName"];
    }
    return 0;
}

- (IBAction)btn_PickerCancel:(id)sender{
    [view_Picker removeFromSuperview];
    [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
}

// select category on Done button click.......
- (IBAction)btn_PickerDone:(id)sender{
    [view_Picker removeFromSuperview];
    
    if (btnComman == BtnCategory){
        NSDictionary * dict = [arrCategoryListing objectAtIndex:[picker_mypicker selectedRowInComponent:0]];
        if([obNet isObject:dict String_1_Array_2_Dictionary_3_Integer_4_UIImage_5:3]){
            tf_CateGoryName.text = [dict objectForKey:@"CategoryName"];
            CategoryId = [[dict objectForKey:@"CategoryId"] intValue];
        }
    }
    [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
}

// here open picker for category listing........
- (IBAction)btn_openPicker:(id)sender{
    [tf_Price resignFirstResponder];
    sv_ScrollView.contentSize = CGSizeMake(0,0);
    
    UIButton * btn = (UIButton *) sender;
    btnComman = btn;
    boolCategory = NO;
    
    if(btn == BtnCategory){
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

#pragma mark - Button action View_MyView Detials
- (IBAction)BtnNearByPlace:(id)sender {
    
    NSString * itemName = [NSString stringWithFormat:@"%@",tf_ItemName.text];
    NSString * Meaasge = [NSString stringWithFormat:@"%@",tv_Message.text];
    
    NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
    dd = [KAppDelegate.dictCategoryInfo mutableCopy];
    if(itemName.length > 0) {
        @try {
            [dd setObject:itemName forKey:@"itemName"];
        } @catch (NSException *exception) {}
    }
    
    if(Meaasge.length > 0) {
        @try {
            [dd setObject:Meaasge forKey:@"ProductDescription"];
        } @catch (NSException *exception) {}
    }
    KAppDelegate.dictCategoryInfo = dd;
    
    static NSInteger dir = 0;
    NearByViewController * near = [[NearByViewController alloc]initWithNibName:@"NearByViewController" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    near.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:near animated:YES];
}

- (IBAction)BtnCross:(id)sender {
    [btnNearByPlace setTitle:@"" forState:UIControlStateNormal];
}

- (IBAction)BtnRightView:(id)sender {
    NSString * itemName = [NSString stringWithFormat:@"%@",tf_ItemName.text];
    NSString * Meaasge = [NSString stringWithFormat:@"%@",tv_Message.text];
    
    NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
    dict = [KAppDelegate.DictEditProductInfo mutableCopy];
    if(dict.count > 0) {
        @try {
            [dict setObject:itemName forKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            [dict setObject:Meaasge forKey:@"ProductDescription"];
        } @catch (NSException *exception) {}
        
        @try {
            KAppDelegate.DictEditProductInfo = dict;
        } @catch (NSException *exception) {}
    }
    
    tf_SellingItemName.text = [NSString stringWithFormat:@"%@",tf_ItemName.text];
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_Price];
    [view_MyView removeFromSuperview];
}

- (IBAction)BtnRemoveView:(id)sender {
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_Price];
    [view_MyView removeFromSuperview];
}

- (IBAction)btnPriceReturnKeyboard:(id)sender {
    sv_ScrollView.contentSize = CGSizeMake(0,0);
    [tf_Price resignFirstResponder];
}

#pragma mark - textView Delegate Method
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    return YES;
}

- (void)textViewDidBeginEditing:(UITextView *)textView
{
    if (textView.frame.origin.y >= KeyboardHeight-20) {
        [sv_ScrollView setContentOffset:CGPointMake(0, textView.frame.origin.y-KeyboardHeight+60) animated:YES];
        [tv_Message setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
    }
    
    if ([textView.text isEqualToString:@"(Optional)                                                     Describe more about what you are selling."]) {
        tv_Message.text = @"";
        tv_Message.textColor = [UIColor darkGrayColor]; //optional
    }
    [textView becomeFirstResponder];
}

- (void)textViewDidEndEditing:(UITextView *)textView
{
    if ([textView.text isEqualToString:@""]) {
        textView.textColor = [UIColor darkGrayColor];
    }
    [textView resignFirstResponder];
}

#pragma mark - textfield delegate methods
-(BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    if( textField == tf_Price) {
        double currentValue = [tf_Price.text doubleValue];
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
        tf_Price.text = [NSString stringWithFormat:@"%.2f", cents / 100.0f];
        
        return NO;
    }
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *) textField
{
    if (tf_Price) {
        [tf_Price resignFirstResponder];
        sv_ScrollView.contentSize = CGSizeMake(0,0);
    }
    
    [textField resignFirstResponder];
    [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
    return YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    if(tf_ItemName) {
        if (textField.frame.origin.y >= KeyboardHeight)
            [sv_ScrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight) animated:YES];
        [tf_ItemName setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
    }
    
    if(textField == tf_Price) {
        [sv_ScrollView setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight) animated:YES];
        [tf_Price setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
        
        if(IS_IPHONE_5) {
            sv_ScrollView.contentSize = CGSizeMake(320, 690);
        } else {
            sv_ScrollView.contentSize = CGSizeMake(320, 750);
        }
    }
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol

- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        
        UITextView * tv = (UITextView *)[self.formItems objectAtIndex:i];
        if(tv == tv_Message){
            [tv_Message resignFirstResponder];
            [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
        }
        
        if(tf == tf_Price){
            [tf_Price resignFirstResponder];
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
        
        if(tf == tf_Price){
            [tf_Price resignFirstResponder];
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

- (void) resignResponder {
    [tf_Price resignFirstResponder];
    [tf_ItemName resignFirstResponder];
    [tv_Message resignFirstResponder];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - EditPhoto View Button actions
- (IBAction)BtnBackView:(id)sender {
    [view_EditPhoto removeFromSuperview];
}

- (IBAction)btnCamera:(id)sender {
    static NSInteger dir = 0;
    CameraTest * camera = [[CameraTest alloc]initWithNibName:@"CameraTest" bundle:nil];
    dir++;
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    camera.hidesBottomBarWhenPushed = NO;
    
    if(tag == 1) {
        KAppDelegate.boolImage1 = YES;
        KAppDelegate.boolImageSelect = NO;
    } else {
        KAppDelegate.boolImageSelect = YES;
        if(tag == 1) {
            KAppDelegate.boolImage1 = YES;
        }
        
        if(tag == 2) {
            KAppDelegate.boolImage2 = YES;
        }
        
        if(tag == 3) {
            KAppDelegate.boolImage3 = YES;
        }
        
        if(tag == 4) {
            KAppDelegate.boolImage4 = YES;
        }
    }
    
    KAppDelegate.boolPopController = YES;
    [self.navigationController pushViewController:camera animated:NO];
}

- (IBAction)BtnEdit:(id)sender {
    if(tag == 1) {
        KAppDelegate.boolImage1 = YES;
    }
    
    if(tag == 2) {
        KAppDelegate.boolImage2 = YES;
    }
    
    if(tag == 3) {
        KAppDelegate.boolImage3 = YES;
    }
    
    if(tag == 4) {
        KAppDelegate.boolImage4 = YES;
    }
    [self sendToAviary];
}

- (IBAction)BtnDelete:(id)sender {
    
    NSMutableArray * arr = [[NSMutableArray alloc]init];
    if(KAppDelegate.DictEditProductInfo.count > 0) {
        arr = [KAppDelegate.DictEditProductInfo objectForKey:@"ProductImageInfo"];
        NSLog(@"arr--> %@",arr);
    }
    
    if(tag == 2) {
        KAppDelegate.boolImage2 = NO;
        NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%ld", (long)tag]];
        if (str) {
            if (str.length > 0) {
                [self removeImage:str];
            }
        } else {
            @try {
                if(arr.count > 1) {
                    NSString * str = [[arr objectAtIndex:1] objectForKey:@"ProImageId"];
                    [KAppDelegate.dictDeleteProductId setObject:str forKey:@"ProImageId1"];
                    NSLog(@"%@",KAppDelegate.dictDeleteProductId);
                }
            } @catch (NSException *exception) {}
        }
        imgProduct2.image = [UIImage imageNamed:@"camera_add_btn.png"];
    }
    
    if(tag == 3) {
         KAppDelegate.boolImage3 = NO;
        NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%ld", (long)tag]];
        if (str) {
            if (str.length > 0) {
                [self removeImage:str];
                
                @try {
                    if(arr.count > 2) {
                        NSString * str = [[arr objectAtIndex:2] objectForKey:@"ProImageId"];
                        [KAppDelegate.dictDeleteProductId setObject:str forKey:@"ProImageId2"];
                    }
                } @catch (NSException *exception) {}
            }
        }
        
        imgProduct3.image = [UIImage imageNamed:@"camera_add_btn.png"];
    }
    
    if(tag == 4) {
        KAppDelegate.boolImage4 = NO;
        NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%ld", (long)tag]];
        if (str) {
            if (str.length > 0) {
                [self removeImage:str];
                
                @try {
                    if(arr.count > 3) {
                        NSString * str = [[arr objectAtIndex:3] objectForKey:@"ProImageId"];
                        [KAppDelegate.dictDeleteProductId setObject:str forKey:@"ProImageId3"];
                    }
                } @catch (NSException *exception) {}
            }
        }
        
        imgProduct4.image = [UIImage imageNamed:@"camera_add_btn.png"];
    }
    
    [view_EditPhoto removeFromSuperview];
}

- (IBAction)MerkAsSold:(id)sender {
    
    NSString * ProductId ;
    NSString * ProductName ;
    NSString * ProductImage ;
    
    if(dictProductInfo.count > 0) {
        @try {
            ProductId = [dictProductInfo objectForKey:@"ProductId"];
        } @catch (NSException *exception) {}
        
        @try {
            ProductName = [dictProductInfo objectForKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            NSArray * arr = [dictProductInfo objectForKey:@"ProductImageInfo"];
            if(arr.count > 0) {
                ProductImage = [dictProductInfo objectForKey:@"ProImageName"];
            }
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&ProductId=%@&ProductName=%@&ProductImage=%@&FromUserId=%@&FromUserName=%@&FromUserImage=%@",[KAppDelegate getDictServer:WS_MarkAsSold],ProductId,ProductName,ProductImage,FromUserId,FromUserName,FromUserImage];
        NSLog(@"MarkAsSold Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"MarkAsSold Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    view_MarkAsSold.frame = self.view.frame;
                    [self.view addSubview:view_MarkAsSold];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

- (IBAction)DeleteProduct:(id)sender {
    NSString * ProductId;
    NSString * ProductName ;
    NSString * ProductImage ;
    
    if(dictProductInfo.count > 0) {
        @try {
            ProductId = [dictProductInfo objectForKey:@"ProductId"];
        } @catch (NSException *exception) {}
        
        @try {
            ProductName = [dictProductInfo objectForKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            NSArray * arr = [dictProductInfo objectForKey:@"ProductImageInfo"];
            if(arr.count > 0) {
                ProductImage = [dictProductInfo objectForKey:@"ProImageName"];
            }
        } @catch (NSException *exception) {}
    }
    
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@&ProductId=%@&ProductName=%@&ProductImage=%@&FromUserId=%@&FromUserName=%@&FromUserImage=%@",[KAppDelegate getDictServer:WS_DeleteProduct],ProductId,ProductName,ProductImage,FromUserId,FromUserName,FromUserImage];
        NSLog(@"DeleteProduct Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            NSLog(@"DeleteProduct Json value-->%@",json);
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    View_Delete.frame = self.view.frame;
                    [self.view addSubview:View_Delete];
                } else {
                    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"" message:[json objectForKey:@"msg"] delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
                    [alert show];
                }
            }
        }];
    }
}

- (IBAction)BtnYesDelete:(id)sender {
     NSString * ProductId;
    KAppDelegate.DictEditProductInfo = [[NSMutableDictionary alloc]init];
    KAppDelegate.dictCategoryInfo = [[NSMutableDictionary alloc]init];
    KAppDelegate.dictImages = [[NSMutableDictionary alloc]init];
    KAppDelegate.boolImageSelect = NO;
    KAppDelegate.boolForEditProduct = NO;
    KAppDelegate.imgCoverPhoto = nil;
    [self.navigationController popViewControllerAnimated:NO];
}

- (IBAction)BtnNoDelete:(id)sender {
    [View_Delete removeFromSuperview];
}

- (IBAction)BtnYesSold:(id)sender {
    NSString * ProductId;
    KAppDelegate.DictEditProductInfo = [[NSMutableDictionary alloc]init];
    KAppDelegate.dictCategoryInfo = [[NSMutableDictionary alloc]init];
    KAppDelegate.dictImages = [[NSMutableDictionary alloc]init];
    KAppDelegate.boolImageSelect = NO;
    KAppDelegate.boolForEditProduct = NO;
    KAppDelegate.imgCoverPhoto = nil;
    [self.navigationController popViewControllerAnimated:NO];
}

- (IBAction)BtnNoSold:(id)sender {
    [view_MarkAsSold removeFromSuperview];
}

- (void)removeImage:(NSString *)fileName
{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *imageName = [NSString stringWithFormat:@"%@",fileName];
    [fileManager removeItemAtPath:imageName error:NULL];
}

#pragma mark - Aviary Delegate method
// here we add effects on image.......
-(void) sendToAviary {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        [AFPhotoEditorController setAPIKey:@"da7cee16e27a7aa0" secret:@"1e981182381d1bd9"];
    });
    AFPhotoEditorController * photoEditor = [[AFPhotoEditorController alloc] initWithImage:img_EditPhoto.image];
    [photoEditor setDelegate:self];
    [self presentViewController:photoEditor animated:NO completion:^{}];
}

- (void) photoEditor:(AFPhotoEditorController *)editor finishedWithImage:(UIImage *)image
{
    [self dismissViewControllerAnimated:YES completion:^{
        img_EditPhoto.image = image;
        
        if(tag == 1) {
            imgProduct1.image = img_EditPhoto.image;
        } else {
            imgProduct = img_EditPhoto.image;
            
            if (imgProduct) {
                if (KAppDelegate.dictImages) {
                    NSString * currentbutton = KAppDelegate.dictImages[CurrentButton];
                    
                    if (currentbutton) {
                        NSString * url = [self SaveUIImage:imgProduct];
                        
                        if (url)
                            KAppDelegate.dictImages[currentbutton] = url;
                    }
                }
            }
            [self SetAllButtonImages];
        }
        [view_EditPhoto removeFromSuperview];
    }];
}

// This is called when the user taps "Cancel" in the photo editor.
- (void) photoEditorCanceled:(AFPhotoEditorController *)editor
{
    [self dismissViewControllerAnimated:YES completion:^{
        [self.navigationController popViewControllerAnimated:NO];
    }];
}

@end
