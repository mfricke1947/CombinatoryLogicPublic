5/28/17

Parantheses omitting

(((a b) c) d)

((a b) c) d


guess, any application to the left of parentheses means that the parentheses is needed


(((a b) c) d)  none needed

 ((a b) (c d))  gives a b (c d)







Let L be operator immediately left of the left parenthesis, or nil
Let R be operator immediately right of the right parenthesis, or nil
If L is nil and R is nil:
  Redundant
Else:
  Scan the unparenthesized operators between the parentheses
  Let X be the lowest priority operator
  If X has lower priority than L or R:
    Not redundant
  Else:
    Redundant
    
    -------
    
 ((a b) (c d))