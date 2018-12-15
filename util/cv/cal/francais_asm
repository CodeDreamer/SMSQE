* SMS2 calendar table for France        v0.01   Mar 1988  J.R.Oakley  QJUMP
*
        section calendar
*
        xref    cv_montb
        xref    cv_jleap
        xref    cv_jdivt
        xref    cv_gleap
        xref    cv_gdivt
*
        xdef    cv_francais
*
cv_francais
        dc.w    cln_eeng-*-2
        dc.b    'Franˆais'
cln_eeng
        dc.w    2                       ; two tables
*
        dc.l    $7ffd9c9a               ; this set valid to this day
        dc.l    1582                    ; which is this year...
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
        dc.l    1582                    ; in this year...
        dc.w    12                      ; ...this month
        dc.w    10,10                   ; 10 days missing, starting at the 10th
cd2_engl
        dc.w    0                       ; no (more) discontinuities
*
        end

