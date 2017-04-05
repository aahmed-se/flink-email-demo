Notes

For email parsing we are using the java mime library from java mail , it seems to work well and we check for filter by null fields depending on the computation.

Email count by day , direct and broadcast are relatively simple computations , the complex one is fastest response for which we have to make some assumptions.

The approach I am using is try to build conversation threads , essentially pair to receiver sender addresses , sorted with a normalized subject 
( removing "re:" and "fe:" tags) we then group element by these , sort by sent date and and if the thread count is more than 1 we return the difference of time
between the first and second sorted group, entry .

In theory this should work but doesn't take care of issues like sent to self and not all edge cases are dealt also , generating all pair of mappings is expensive but the overall code is simple.


Execute:

gradle -q run -PemailDir="/Users/a.ahmed/Downloads/enron_with_categories"