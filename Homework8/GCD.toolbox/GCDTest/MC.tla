---- MODULE MC ----
EXTENDS GCD, TLC

\* CONSTANT definition @modelParameterDefinitions:0
def_ov_149410885357334000 ==
-1000..1000
----
\* Constant expression definition @modelExpressionEval
const_expr_149410885357335000 == 
GCD(13, 20)
----

\* Constant expression ASSUME statement @modelExpressionEval
ASSUME PrintT(<<"$!@$!@$!@$!@$!",const_expr_149410885357335000>>)
----

=============================================================================
\* Modification History
\* Created Sat May 06 18:14:13 EDT 2017 by trittimo
