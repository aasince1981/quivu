//
//  AddItemScreen.m
//  quivu
//
//  Created by Kamal on 12/06/14.
//  Copyright (c) 2014 Idealittechno. All rights reserved.
//

#import "AddItemScreen.h"
#import "NearByViewController.h"
#import "CameraTest.h"
#import "MeViewController.h"

@interface AddItemScreen ()
{
    int tag;
}
@end

#define CurrentButton @"currentbutton"

@implementation AddItemScreen
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
    [tf_Price setTag:1];
    
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
    
    boolTwitterImagePost = NO;
    boolFacebookImagePost = NO;
}

// set textField font type and size..........
- (void) drawPlaceholderInRect:(CGRect)rect {
    [tf_CateGoryName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_SellingItemName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_Price.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
    [tf_ItemName.placeholder drawInRect:rect withFont:FONT_Lato_Light(14.0f)];
}

// set font type and size..........
- (void) SetFontType {
    lblSell.font = FONT_Lato_Bold(20.0f);
    lblCoverPhoto.font = FONT_Lato_Bold(13.0f);
    lblDetails.font = FONT_Lato_Bold(17.0f);
    lblCategory.font = FONT_Lato_Light(17.0f);
    lblItem.font = FONT_Lato_Light(17.0f);
    lblPrice.font = FONT_Lato_Light(17.0f);
    lblSharing.font = FONT_Lato_Bold(17.0f);
    lblTwitter.font = FONT_Lato_Light(17.0f);
    lblFacebookWall.font = FONT_Lato_Light(17.0f);
    lblFacebookPage.font = FONT_Lato_Light(17.0f);
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
    
    if(KAppDelegate.imgCoverPhoto != nil)
        [imgProduct1 setImage:KAppDelegate.imgCoverPhoto];
    
    if(KAppDelegate.NearByPlace != nil)
        [btnNearByPlace setTitle:KAppDelegate.NearByPlace forState:UIControlStateNormal];
    
    if (imgProduct) {
        if (KAppDelegate.dictImages) {
            NSString * currentbutton = KAppDelegate.dictImages[CurrentButton];
            
            if (currentbutton) {
                NSString * url = [self SaveUIImage:imgProduct];
                imgProduct = nil;
                if (url)
                    KAppDelegate.dictImages[currentbutton] = url;
            }
        }
    }
    
    //NSLog(@"KAppDelegate.dictCategoryInfo--> %@",KAppDelegate.dictCategoryInfo);
    
    if(KAppDelegate.dictCategoryInfo.count > 0) {
        tf_CateGoryName.text = [KAppDelegate.dictCategoryInfo objectForKey:@"CategoryName"];
        tf_SellingItemName.text = [KAppDelegate.dictCategoryInfo objectForKey:@"itemName"];
        tf_ItemName.text = [KAppDelegate.dictCategoryInfo objectForKey:@"itemName"];
        if([KAppDelegate.dictCategoryInfo objectForKey:@"price"] == nil) {
            tf_Price.text = @"";
        } else {
            tf_Price.text = [KAppDelegate.dictCategoryInfo objectForKey:@"price"];
        }
        tv_Message.text = [KAppDelegate.dictCategoryInfo objectForKey:@"ProductDescription"];
    }
    
    [self SetAllButtonImages];
}

// set all Button images here..........
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
                        case 1:
                            imgProduct1.image = img;
                            break;
                            
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

#pragma mark - CategoryListing
// get Category Listing here...........
-(void) GetCategoriesList {
    if([obNet InternetStatus:YES]) {
        NSString *url = [NSString stringWithFormat:@"%@",[KAppDelegate getDictServer:WS_GetCategories]];
        NSLog(@"GetCategories Url-->%@",url);
        
        [obNet JSONFromWebServices:url Parameter:nil AI:NO PopUP:NO WithBlock:^(id json) {
            
            if(json != nil) {
                if([[json objectForKey:@"success"] intValue] == 1) {
                    arrCategoryListing = [json objectForKey:@"CategoryList"];
                    
                    if(dictProductInfo.count > 0) {
                        for(int i=0;i<arrCategoryListing.count; i++) {
                            NSDictionary * dict = [arrCategoryListing objectAtIndex:i];
                            NSString * CategoryName = [dictProductInfo objectForKey:@"CategoryName"];
                            
                            if([CategoryName isEqualToString:[dict objectForKey:@"CategoryName"]]) {
                                NSString * str = [NSString stringWithFormat:@"%d",[[dict objectForKey:@"CategoryId"] intValue]];
                                KAppDelegate.CategoryId = [str intValue];
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
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tf_ItemName];
    [self.formItems addObject:tv_Message];
    view_MyView.frame = self.view.frame;
    [self.view addSubview:view_MyView];
}

// here we call add another image or edit image.........
- (IBAction)BtnAddProductImage:(id)sender {
    
    // get tag of particular button......
        UIButton * btn = (UIButton *) sender;
    tag = btn.tag;
    
    @try {
        NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
        dd = [KAppDelegate.dictCategoryInfo mutableCopy];
        NSString * str = [NSString stringWithFormat:@"%@",tf_Price.text];
        
        if(str.length > 0) {
            @try {
                [dd setObject:str forKey:@"price"];
            } @catch (NSException *exception) {}
            
            KAppDelegate.dictCategoryInfo = dd;
        }
    } @catch (NSException *exception) {}
    
    KAppDelegate.boolPopController = YES;
    
    if (!KAppDelegate.dictImages)
        KAppDelegate.dictImages = [NSMutableDictionary new];
    
    KAppDelegate.dictImages[CurrentButton] = [NSString stringWithFormat:@"%ld", (long)btn.tag];
    
    static NSInteger dir = 0;
    CameraTest * camera = [[CameraTest alloc]initWithNibName:@"CameraTest" bundle:nil];
    dir++;
    
    KAppDelegate.leveyTabBarController.animateDriect = dir % 2;
    camera.hidesBottomBarWhenPushed = NO;
    
    if(btn.tag == 1) {
        KAppDelegate.boolImageSelect = NO;
        
        if([imgProduct1.image isEqual:[UIImage imageNamed:@"camera_add_btn.png"]]) {
            [self.navigationController pushViewController:camera animated:NO];
            
        } else {
            imgEdit.frame = CGRectMake(imgDelete.frame.origin.x, imgEdit.frame.origin.y, imgEdit.frame.size.width, imgEdit.frame.size.height);
            btnEdit.frame= CGRectMake(btnDelete.frame.origin.x, btnEdit.frame.origin.y, btnEdit.frame.size.width, btnEdit.frame.size.height);
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
    KAppDelegate.boolPopController = NO;
    [self.navigationController popViewControllerAnimated:YES];
}

// here we call addItem webservice........
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
            NSString * webservice;
            
            webservice = [NSString stringWithFormat:@"%@&ProductCatName=%@&ProductCatId=%d&ProductPrice=%@&ProductName=%@&ProductDescription=%@&ProductLatitude=%@&ProductLongitude=%@&ProductUserId=%@&ProductUserName=%@&ProductUserImage=%@&ProductAddress=%@",[KAppDelegate getDictServer:WS_AddProduct],tf_CateGoryName.text,KAppDelegate.CategoryId,price,tf_SellingItemName.text,tv_Message.text,latitude,longitude,UserId,UserName,UserImage,address];
            NSLog(@"AddProduct Url-->%@",webservice);
            
            NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
            [dateFormatter setDateFormat:@"yyyy-MM-ddHH:mm:ss"];
            NSLog(@"System time: profileimage_0%@", [dateFormatter stringFromDate:[NSDate date]]);
            
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
            
            // here we upload images to server..........
            if (fileData1) {
                [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage1\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:fileData1];
                [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
            }
            
            if (fileData2) {
                [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage2\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:fileData2];
                [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
            }
            
            if (fileData3) {
                [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage3\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:fileData3];
                [body appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
            }
            
            if (fileData4) {
                [body appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"ProductImage4\"; filename=\"%@\"\r\n", fileName] dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:[@"Content-Type: application/octet-stream\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
                [body appendData:fileData4];
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
                NSLog(@"AddProduct Url-->%@",webservice);
                
                NSLog(@"UploadFileWithFileDataJSON = %@, e-%@", dictJSON, e);
                
                if (dictJSON != nil) {
                    if ([[dictJSON objectForKey:@"success"]intValue] == 1) {
                        if(boolTwitterImagePost == YES) {
                            [self PostImageOnTwitter];
                        }
                        
                        if(boolFacebookImagePost == YES) {
                            [self openSessionWithAllowLoginUI:YES];
                        }
                        
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
        KAppDelegate.dictCategoryInfo = [[NSMutableDictionary alloc]init];
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
            KAppDelegate.CategoryId = [[dict objectForKey:@"CategoryId"] intValue];
            
            NSMutableDictionary * dd = [[NSMutableDictionary alloc]init];
            dd = [KAppDelegate.dictCategoryInfo mutableCopy];
            NSString * str = [NSString stringWithFormat:@"%@",tf_CateGoryName.text];
            
            if(str.length > 0) {
                @try {
                    [dd setObject:str forKey:@"CategoryName"];
                } @catch (NSException *exception) {}
                
                KAppDelegate.dictCategoryInfo = dd;
            }
        }
    }
    
    [sv_ScrollView setContentOffset:CGPointMake(0, 0) animated:YES];
}

// here open picker for category listing........
- (IBAction)btn_openPicker:(id)sender{
    [tf_Price resignFirstResponder];
    
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
    
    NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
    dict = [dictProductInfo mutableCopy];
    if(dict.count > 0) {
        @try {
            [dict setObject:itemName forKey:@"ProductName"];
        } @catch (NSException *exception) {}
        
        @try {
            [dict setObject:Meaasge forKey:@"ProductDescription"];
        } @catch (NSException *exception) {}
        
        @try {
            dictProductInfo = dict;
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
    if(textField == tf_ItemName) {
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
        KAppDelegate.boolImageSelect = NO;
    } else {
        KAppDelegate.boolImageSelect = YES;
    }
    KAppDelegate.boolPopController = YES;
    [self.navigationController pushViewController:camera animated:NO];
}

- (IBAction)BtnEdit:(id)sender {
    [self sendToAviary];
}

- (IBAction)BtnDelete:(id)sender {
    
    if(tag == 2) {
        NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%d", tag]];
        if (str) {
            if (str.length > 0) {
                [self removeImage:str];
            }
        }
       
        imgProduct2.image = [UIImage imageNamed:@"camera_add_btn.png"];
    }
    
    if(tag == 3) {
        NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%d", tag]];
        if (str) {
            if (str.length > 0) {
                [self removeImage:str];
            }
        }
        
        imgProduct3.image = [UIImage imageNamed:@"camera_add_btn.png"];
    }
    
    if(tag == 4) {
        NSString * str = KAppDelegate.dictImages[[NSString stringWithFormat:@"%d", tag]];
        if (str) {
            if (str.length > 0) {
                [self removeImage:str];
            }
        }
        
        imgProduct4.image = [UIImage imageNamed:@"camera_add_btn.png"];
    }
    
    [view_EditPhoto removeFromSuperview];
}

- (IBAction)btnTwitter:(id)sender {
    if(boolTwitter) {
        [btnTwitter setBackgroundImage:[UIImage imageNamed:@"off_btn.png"] forState:UIControlStateNormal];
        boolTwitterImagePost = NO;
    } else {
        [btnTwitter setBackgroundImage:[UIImage imageNamed:@"on_btn.png"] forState:UIControlStateNormal];
        boolTwitterImagePost = YES;
    }
    
     boolTwitter = !boolTwitter;
}

- (IBAction)btnFacebook:(id)sender {
    
    if(boolFacebook) {
        [btnFacebook setBackgroundImage:[UIImage imageNamed:@"on_btn.png"] forState:UIControlStateNormal];
        boolFacebookImagePost = YES;
    } else {
        [btnFacebook setBackgroundImage:[UIImage imageNamed:@"off_btn.png"] forState:UIControlStateNormal];
        boolFacebookImagePost = NO;
    }
    boolFacebook = !boolFacebook;
}

- (IBAction)btnPriceReturnKeyboard:(id)sender {
    sv_ScrollView.contentSize = CGSizeMake(0,0);
    [tf_Price resignFirstResponder];
}

// here remove image on delete button click...........
- (void)removeImage:(NSString *)fileName
{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *imageName = [NSString stringWithFormat:@"%@",fileName];
    [fileManager removeItemAtPath:imageName error:NULL];
}

#pragma mark - Aviary Delegate method
// here we call to add effects on images.......
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

#pragma  mark - PostImages on Twitter
// Here we post image to Twitter......
- (void)PostImageOnTwitter
{
    if ([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter])
    {
        ACAccountStore *account = [[ACAccountStore alloc] init];
        ACAccountType *accountType = [account accountTypeWithAccountTypeIdentifier:ACAccountTypeIdentifierTwitter];
        
        NSArray *arrayOfAccons = [account accountsWithAccountType:accountType];
        for(ACAccount *acc in arrayOfAccons)
        {
            NSLog(@"%@",acc.username);
            NSLog(@"%@",acc);
        }
        
        [account requestAccessToAccountsWithType:accountType withCompletionHandler:^(BOOL granted, NSError *error)
         {
             if (granted == YES) {
                 NSArray *arrayOfAccounts = [account accountsWithAccountType:accountType];
                 
                 if ([arrayOfAccounts count] > 0) {
                     
                     ACAccount *acct = [arrayOfAccounts objectAtIndex:0];
                     TWRequest *postRequest = [[TWRequest alloc] initWithURL:[NSURL URLWithString:@"https://upload.twitter.com/1/statuses/update_with_media.json"] parameters:nil requestMethod:TWRequestMethodPOST];
                     UIImage *img = imgProduct1.image;
                     NSData *myData = UIImagePNGRepresentation(img);
                     
                     [postRequest addMultiPartData:myData withName:@"media" type:@"image/png"];
                     myData = [[NSString stringWithFormat:@""] dataUsingEncoding:NSUTF8StringEncoding];
                     
                     [postRequest addMultiPartData:myData withName:@"status" type:@"text/plain"];
                     
                     [postRequest setAccount:acct];
                     //[postRequest setAccount:acct1];
                     
                     [postRequest performRequestWithHandler:^(NSData *responseData, NSHTTPURLResponse *urlResponse, NSError *error)
                      {
                          if(error) {
                              UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"Error in posting" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                              [alert show];
                          } else {
                              NSLog(@"Twitter response, HTTP response: %li", (long)[urlResponse statusCode]);
                          }
                      }];
                 } else {
                     UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"You have no twitter account" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                     [alert show];
                 }
             } else {
                 UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"Permission not granted" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                 [alert show];
             }
         }];
        //  [widgetsHandler closeWidget:nil];
    } else {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Twitter" message:@"You have no twitter account" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    }
}

#pragma  mark - PostImages on Facebook
// Here we post image to Facebbok......
- (BOOL)openSessionWithAllowLoginUI:(BOOL)allowLoginUI {
    
    NSArray * permissions = [[NSArray alloc] initWithObjects: @"publish_stream",nil];
    return [FBSession openActiveSessionWithPublishPermissions:permissions defaultAudience:FBSessionDefaultAudienceEveryone  allowLoginUI:allowLoginUI completionHandler:^(FBSession *session,FBSessionState state,NSError *error) {[self sessionStateChanged:session state:state error:error];
    }];
}

// check session of facebook........
-(void)sessionStateChanged:(FBSession *)session state:(FBSessionState) state error:(NSError *)error
{
    if (!error && state == FBSessionStateOpen){
        NSLog(@"Session opened");
        [[FBRequest requestForMe] startWithCompletionHandler:^(FBRequestConnection *connection, NSDictionary<FBGraphUser> *user, NSError *error) {
            [self PostImageOnFacebook];
        }];
        return;
    }
    if (state == FBSessionStateClosed || state == FBSessionStateClosedLoginFailed){
        NSLog(@"Session closed");
    }
    
    if (error){
        NSLog(@"Error");
        NSString *alertText;
        NSString *alertTitle;
        if ([FBErrorUtility shouldNotifyUserForError:error] == YES){
            alertTitle = @"Something went wrong";
            alertText = [FBErrorUtility userMessageForError:error];
        } else {
            
            if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryUserCancelled) {
                NSLog(@"User cancelled login");
                
            } else if ([FBErrorUtility errorCategoryForError:error] == FBErrorCategoryAuthenticationReopenSession){
                alertTitle = @"Session Error";
                alertText = @"Your current session is no longer valid. Please log in again.";
                
            } else {
                NSDictionary *errorInformation = [[[error.userInfo objectForKey:@"com.facebook.sdk:ParsedJSONResponseKey"] objectForKey:@"body"] objectForKey:@"error"];
                
                alertTitle = @"Something went wrong";
                alertText = [NSString stringWithFormat:@"Please retry. \n\n If the problem persists contact us and mention this error code: %@", [errorInformation objectForKey:@"message"]];
            }
        }
        [FBSession.activeSession closeAndClearTokenInformation];
    }
}

- (void) PostImageOnFacebook {

    UIImage * imgPost = [[UIImage alloc] init];
    imgPost = imgProduct1.image;
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc] init];
    [params setObject:UIImagePNGRepresentation(imgPost) forKey:@"picture"];
    
    [FBRequestConnection startWithGraphPath:@"/me/photos"
                                 parameters:params
                                 HTTPMethod:@"POST"
                          completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
                              if (!error) {
                                  UIAlertView *tmp = [[UIAlertView alloc]  initWithTitle:@"" message:@"SuccessFully post" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
                                  //[tmp show];
                              } else {
                                  NSLog(@"error--> %@",error);
                                  UIAlertView *tmp = [[UIAlertView alloc]  initWithTitle:@"" message:@"Some error happened" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
                                  //[tmp show];
                              }
                          }];
    
    NSMutableDictionary *variables = [NSMutableDictionary dictionaryWithCapacity:3];
}

@end
