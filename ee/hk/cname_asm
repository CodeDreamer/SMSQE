; HOTKEY Compare (incomplete) names   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_cname

        xref    cv_ctype
        xref    cv_strdf

        include 'dev8_keys_k'
        include 'dev8_keys_err'
        include 'dev8_mac_assert'

;+++
; This routine compares a complete string against a head substring.
;
;       a0 c  p check all of this string
;       a1 c  p does this start with (a0)? 
;       error returns 0 or err.fdnf
;---
hk_cname
        jsr     cv_strdf                 ; compare
        beq.s   hcn_exit                 ; that's it
        swap    d0
        tst.w   d0                       ; all of given name?
        bne.s   hcn_fdnf                 ; ... no
        swap    d0
        exg     d1,d0
        move.b  1(a1,d1.w),d1            ; offending character
        jsr     cv_ctype
        exg     d0,d1
        assert  k.lclet-4,k.uclet-6
        subq.b  #k.lclet,d0
        cmp.b   #k.uclet-k.lclet,d0      ; uc letter or lc letter
        bls.s   hcn_fdnf                 ; ... no
        moveq   #0,d0
        bra.s   hcn_exit
hcn_fdnf
        moveq   #err.fdnf,d0
hcn_exit
        rts
        end
