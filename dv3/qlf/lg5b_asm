; DV3 QL5B Logical to Physical Translate  V3.00           1992 Tony Tebby

        section dv3

        xdef    qlf_lg5b
        xdef    qlf_ls5b

        include 'dev8_keys_ql5b'
        include 'dev8_dv3_keys'
        include 'dev8_dv3_qlf_keys'
        include 'dev8_keys_err'

;+++
; DV3 QL5B logical group / sector to physical sector translate.
; Cylinder / head / sector format only.
;
;       d0 cr   logical group / physical sector
;       d1 c  p sector in logical group
;       d7 c  p drive ID / number
;       a3 c  p pointer to linkage
;       a4 c  p pointer to drive definition
;
;       status return arbitrary
;
;---
qlf_lg5b
        mulu    ddf_asect(a4),d0
        add.l   d1,d0                    ; physical sector number

;+++
; DV3 QL5B logical to physical sector translate.
; Cylinder / head / sector format only.
;
;       d0 cr   logical sector / physical sector
;       d7 c  p drive ID / number
;       a3 c  p pointer to linkage
;       a4 c  p pointer to drive definition
;
;       status return arbitrary
;
;---
qlf_ls5b
qlg.reg reg    d1/d2
        movem.l qlg.reg,-(sp)

        divu    q5a_scyl+qdf_map(a4),d0  ; sector+side / cylinder
        move.w  d0,d2                    ; cylinder (track)
        swap    d0
        move.w  d0,d1                    ; sector
        add.w   #q5a_lgph+qdf_map,d1
        move.b  (a4,d1.w),d1             ; translated sector
        move.b  d1,d0
        add.w   d0,d0                    ; set side
        and.w   #$7f,d1                  ; (remove side from sector number)

        mulu    q5a_soff+qdf_map(a4),d2  ; sector offset (skew)
        add.w   d1,d2
        divu    q5a_strk+qdf_map(a4),d2  ; in range
        swap    d2
        addq.b  #1,d2                    ; +1 for IBM standard
        move.b  d2,d0                    ; sector

        movem.l (sp)+,qlg.reg
        rts
        end
