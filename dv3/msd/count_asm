; DV3 MSDOS Count Free and Bad Sectors  V3.00           1993 Tony Tebby

        section dv3

        xdef    msd_count

        include 'dev8_keys_dos'
        include 'dev8_dv3_keys'
        include 'dev8_dv3_msd_keys'
        include 'dev8_mac_xword'
        include 'dev8_mac_assert'
;+++
; DV3 MSDOS Count Free and Bad Sectors
;
;       d0  r   0
;       d1  r   free sectors
;       d2  r   bad sectors
;       a3 c  p pointer to linkage
;       a4 c  p pointer to drive definition
;
;       status return zero
;
;---
msd_count
mcn.reg reg     d3/a2
        movem.l mcn.reg,-(sp)

        move.l  mdf_fat(a4),a2           ; base of FAT
        moveq   #0,d3
        move.w  mdf_fent(a4),d3          ; clusters to check
        subq.w  #2,d3                    ; ignoring first two

        moveq   #0,d1
        moveq   #0,d2                    ; counts zero

        assert  ddf.msd3,0
        tst.b   ddf_stype(a4)            ; format subtype
        bne.s   mc4_set                  ; 4 nibble, the easy case
        addq.l  #3,a2                    ; start here
        subq.w  #1,d3                    ; there may be an extra one at the end

mc3_loop
        move.b  (a2)+,d0                 ; could this be empty?
        beq.s   mc3_z1                   ; ... yes
        bpl.s   mc3_ck2                  ; ... it is normal
        addq.b  #8,d0                    ; could it be bad?
        bcs.s   mc3_ck2                  ; ... no, it is end of file
        addq.b  #8,d0                    ; could it be bad?
        bcc.s   mc3_ck2                  ; ... no, it is normal
        moveq   #$fffffff0,d0
        or.b    (a2),d0
        addq.b  #1,d0                    ; ms nibble = $f?
        bne.s   mc3_ck2                  ; ... no
        addq.w  #1,d2                    ; one more bad
        bra.s   mc3_ck2
mc3_z1
        moveq   #$f,d0
        and.b   (a2),d0                  ; is it free?
        bne.s   mc3_ck2                  ; ... no
        addq.w  #1,d1                    ; one more free

mc3_ck2
        moveq   #$fffffff0,d0            ; the other nibble
        and.b   (a2)+,d0
        beq.s   mc3_z2                   ; could be free
        bmi.s   mc3_elp                  ; ... normal or end of file

        cmp.b   #$ff,(a2)                ; $ff0-$ff7
        bne.s   mc3_elp                  ; ... no
        addq.w  #1,d2                    ; one more bad
        bra.s   mc3_elp

mc3_z2
        tst.b   (a2)                     ; the rest zero?
        bne.s   mc3_elp                  ; ... no
        addq.w  #1,d1                    ; one more free
mc3_elp
        addq.l  #1,a2
        subq.l  #2,d3
        bgt.s   mc3_loop
        blt.s   mcn_ok                   ; none left to check


        move.b  (a2)+,d0                 ; could this be empty?
        beq.s   mc3_ze                   ; ... yes
        bpl.s   mcn_ok                   ; ... it is normal
        addq.b  #8,d0                    ; could it be bad?
        bcs.s   mcn_ok                   ; ... no, it is end of file
        addq.b  #8,d0                    ; could it be bad?
        bcc.s   mcn_ok                   ; ... no, it is normal
        moveq   #$fffffff0,d0
        or.b    (a2),d0
        addq.b  #1,d0                    ; ms nibble = $f?
        bne.s   mcn_ok                   ; ... no
        addq.w  #1,d2                    ; one more bad
        bra.s   mcn_ok
mc3_ze
        moveq   #$f,d0
        and.b   (a2),d0                  ; is it free?
        bne.s   mcn_ok                   ; ... no
        addq.w  #1,d1                    ; one more free
        bra.s   mcn_ok

mc4_set
        addq.l  #4,a2                    ; skip silly groups at start
mc4_loop
        move.w  (a2)+,d0                 ; empty?
        bne.s   mc4_ckb
        addq.w  #1,d1                    ; ... yes
        bra.s   mc4_elp

mc4_ckb
        bpl.s   mc4_elp                  ; normal sector
        or.w    #$0700,d0                ; make all bad sector values equal
        cmp.w   #$f7ff,d0                ; bad?
        bne.s   mc4_elp                  ; ... no
        addq.w  #1,d2
mc4_elp
        subq.l  #1,d3
        bgt.s   mc4_loop


mcn_ok
        moveq   #0,d0
        movem.l (sp)+,mcn.reg
        rts

        end
