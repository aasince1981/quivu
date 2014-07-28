/*
//           viewcontroller.h file code
@property (strong, nonatomic) NSMutableArray *formItems;
@property (strong, nonatomic) KSEnhancedKeyboard *enhancedKeyboard;

//           viewcontroller.m file code

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.formItems = [[NSMutableArray alloc] init];
    [self.formItems addObject:tfUsername];
    [self.formItems addObject:tfPassword];
    
    self.enhancedKeyboard = [[KSEnhancedKeyboard alloc] init];
    self.enhancedKeyboard.delegate = self;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    if (textField.frame.origin.y >= KeyboardHeight) {
        [SC_my setContentOffset:CGPointMake(0, textField.frame.origin.y - KeyboardHeight) animated:YES];
    }
    [textField setInputAccessoryView:[self.enhancedKeyboard getToolbarWithPrevEnabled:YES NextEnabled:YES DoneEnabled:YES]];
}

- (BOOL)textFieldShouldReturn:(UITextField *) textField {
    
    [textField resignFirstResponder];
    
    return YES;
}

#pragma mark - KSEnhancedKeyboardDelegate Protocol

- (void)nextDidTouchDown
{
    for (int i=0; i<[self.formItems count]; i++)
    {
        UITextField * tf = (UITextField *)[self.formItems objectAtIndex:i];
        
        if ([tf isEditing] && i!=[self.formItems count]-1)
        {
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
            break;
        }
    }
}*/