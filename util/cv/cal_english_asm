* SMS2 calendar table for England       v0.01   Mar 1988  J.R.Oakley  QJUMP
*
        section calendar
*
        xref    cv_montb
        xref    cv_jleap
        xref    cv_jdivt
        xref    cv_gleap
        xref    cv_gdivt
*
        xdef    cv_english
*
cv_english
        dc.w    cln_eeng-*-2
        dc.b    'English'
cln_eeng
        dc.w    2                       ; two tables
*
        dc.l    $7ffe8f26               ; this set valid to this day
        dc.l    1752                    ; which is this year...
        dc.w    12,31                   ; ...month and day
        dc.l    -44084                  ; fudge factor
        dc.w    cd1_engl-*              ; discontinuities in this range
        dc.w    cv_montb-*              ; days in month
        dc.w    cv_jleap-*              ; leap calculations
        dc.w    cv_jdivt-*              ; constants
*
cv2_engl
        dc.l    $ffffffff               ; this set valid to this day
        dc.l    6000000                 ; which is this year...
        dc.w    0,0                     ; ...month and day
        dc.l    0                       ; fudge factor
        dc.w    cd2_engl-*              ; discontinuities in this range
        dc.w    cv_montb-*              ; days in month
        dc.w    cv_gleap-*              ; leap calculations
        dc.w    cv_gdivt-*              ; constants
*
cd1_engl
        dc.w    1                       ; one discontinuity
        dc.l    1752                    ; in this year...
        dc.w    9                       ; ...this month
        dc.w    3,11                    ; 11 days missing, starting at the 3rd
cd2_engl
        dc.w    0                       ; no (more) discontinuities
*
        end
