package frc.team5499.frc2018Kotlin.auto.routines

import frc.team5499.frc2018Kotlin.auto.routines.Routine
import frc.team5499.frc2018Kotlin.auto.actions.*
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.ArmDirection
import frc.team5499.frc2018Kotlin.auto.actions.ArmAction.IntakeDirection

object Routines {
        var routineList: MutableList<Routine> = mutableListOf<Routine>()
        
        var ro_oc: Routine = Routine("rightouter_onecube")
        var ri_tc: Routine = Routine("rightinner_twocube")
        var m_tc_l: Routine = Routine("middle_twocube_left")
        var m_tc_r: Routine = Routine("middle_twocube_right")
        var li_tc: Routine = Routine("leftinner_twocube")
        var lo_oc: Routine = Routine("leftouter_onecube")
        var baseline: Routine = Routine("baseline")
        var nothing: Routine = Routine("nothing")
        var tuning: Routine = Routine("tuning")
        init {
                routineList.add(ro_oc)
                routineList.add(ri_tc)
                routineList.add(m_tc_l)
                routineList.add(m_tc_r)
                routineList.add(li_tc)
                routineList.add(lo_oc)
                routineList.add(baseline)
                routineList.add(nothing)
                routineList.add(tuning)

                // define routines
                // ro_oc.addAction(NothingCommand(7.0))
                ro_oc.addAction(NothingAction(0.0))
                ro_oc.addAction(ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.4))
                ro_oc.addAction(DriveStraightAction(3.0, -150.0))
                ro_oc.addAction(TurnAction(2.0, 90.0))
                ro_oc.addAction(DriveStraightAction(1.5, -35.0))
                ro_oc.addAction(ArmAction(ArmDirection.NONE, IntakeDirection.DROP, 0.4))

                // lo_oc.addAction(NothingCommand(7.0))
                lo_oc.addAction(NothingAction(0.0))
                lo_oc.addAction(ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.4))
                lo_oc.addAction(DriveStraightAction(3.0, -150.0))
                lo_oc.addAction(TurnAction(1.5, -90.0))
                lo_oc.addAction(DriveStraightAction(1.5, -35.0))
                lo_oc.addAction(ArmAction(ArmDirection.NONE, IntakeDirection.DROP, 0.4))


                // m_tc_l.addAction(NothingCommand(0))
                // m_tc_l.addAction(ArmAction(0, true, true, 110))
                // m_tc_l.addAction(DriveCommand(2, false, -45))
                // m_tc_l.addAction(TurnCommand(2, false, 90))
                // m_tc_l.addAction(DriveCommand(2, false, -42))
                // m_tc_l.addAction(TurnCommand(2, false, -90))
                // m_tc_l.addAction(DriveCommand(1.5, false, -48))
                // m_tc_l.addAction(DriveSlowCommand(1, false, -10))
                // m_tc_l.addAction(ArmAction(0, true, false, 110))
                // m_tc_l.addAction(OuttakeDriveCommand(0.25, true, 0.6))
                // // Two cube section
                // m_tc_l.addAction(DriveCommand(2, false, 65))
                // m_tc_l.addAction(ArmAction(0.5, true, true, -50))
                // m_tc_l.addAction(ArmAction(0, false, true, -50))
                // m_tc_l.addAction(TurnCommand(2, false, 135))
                // m_tc_l.addAction(ArmAction(0, false, false, -25))
                // m_tc_l.addAction(DriveCommand(1.25, false, 48))
                // m_tc_l.addAction(IntakeDriveCommand(1.5, false, 32, -1.0, false))
                // m_tc_l.addAction(IntakeCommand(0.4, -0.6, false))
                // m_tc_l.addAction(ArmAction(0.5, true, true, 110))
                // m_tc_l.addAction(DriveCommand(2, false, -50))
                // m_tc_l.addAction(TurnCommand(2, false, -135))
                // m_tc_l.addAction(DriveCommand(1.8, false, -52))
                // m_tc_l.addAction(DriveSlowCommand(0.5, false, -10))
                // m_tc_l.addAction(ArmAction(0, true, false, 110))
                // m_tc_l.addAction(OuttakeDriveCommand(1, true, 0.4))

                // m_tc_r.addAction(NothingCommand(0))
                // m_tc_r.addAction(ArmAction(0, true, true, 110))
                // m_tc_r.addAction(DriveCommand(2, false, -45))
                // m_tc_r.addAction(TurnCommand(2, false, -90))
                // m_tc_r.addAction(DriveCommand(2, false, -47))
                // m_tc_r.addAction(TurnCommand(2, false, 90))
                // m_tc_r.addAction(DriveCommand(1.5, false, -48))
                // m_tc_r.addAction(DriveSlowCommand(0.5, false, -10))
                // m_tc_r.addAction(ArmAction(0, true, false, 110))
                // m_tc_r.addAction(OuttakeDriveCommand(0.25, true, 0.6))
                // Two cube section
                // m_tc_r.addAction(DriveCommand(2, false, 60))
                // m_tc_r.addAction(ArmAction(0.5, true, true, -50))
                // m_tc_r.addAction(ArmAction(0, false, true, -50))
                // m_tc_r.addAction(TurnCommand(2, false, -135))
                // m_tc_r.addAction(ArmAction(0, false, false, -25))
                // m_tc_r.addAction(DriveCommand(1, false, 35))
                // m_tc_r.addAction(IntakeDriveCommand(1.5, false, 32, -1.0, false))
                // m_tc_r.addAction(IntakeCommand(0.4, -0.6, false))
                // m_tc_r.addAction(ArmAction(0.5, true, true, 110))
                // m_tc_r.addAction(DriveCommand(1.8, false, -50))
                // m_tc_r.addAction(TurnCommand(2, false, 135))
                // m_tc_r.addAction(DriveCommand(1.8, false, -52))
                // m_tc_r.addAction(DriveSlowCommand(0.5, false, -10))
                // m_tc_r.addAction(ArmAction(0, true, false, 110))
                // m_tc_r.addAction(OuttakeDriveCommand(1, true, 0.4))



                // drives 90 inches(just enough to cross baseline)
                // lo_nc.addAction(NothingCommand(7.0))

                baseline.addAction(ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.2))
                baseline.addAction(DriveStraightAction(3.0, -106.0))
                //baseline.addAction(TurnCommand(10, false, 90))

                nothing.addAction(NothingAction(0.0))

                // tuning.addAction(ArmAction(1, true, true, 50))
                //tuning.addAction(ArmAction(1, true, true, 80))
                
                // tuning.addAction(DriveCommand(3, false, -20))
                // tuning.addAction(TurnCommand(2, false, 90))
                // tuning.addAction(ArmAction(0, true, false, 110))
                // tuning.addAction(IntakeDriveCommand(10, false, 200, -1, true))
                // tuning.addAction(NothingCommand(10))
        }

}
